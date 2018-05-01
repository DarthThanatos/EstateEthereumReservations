package ethtest;

import org.adridadou.ethereum.EthereumFacade;
import org.adridadou.ethereum.blockchain.TestConfig;
import org.adridadou.ethereum.keystore.AccountProvider;
import org.adridadou.ethereum.values.EthAccount;
import org.adridadou.ethereum.values.EthValue;

import java.util.HashMap;

import static org.adridadou.ethereum.values.EthValue.ether;

public class AccountsManager {

    private HashMap<String, EthAccount> accounts = new HashMap<>();

    private EthereumFacade ethereum;
    private CoinManager coinManager;
    private ReservationManager reservationManager;

    public void setEthereum(EthereumFacade ethereum){
        this.ethereum = ethereum;
        onInitAfterEthereumAssigned();
    }

    private void onInitAfterEthereumAssigned(){
        ContractPublisher contractPublisher = new ContractPublisher(ethereum);
        EthAccount mainAccount = accounts.get("main");
        initCoinManager(contractPublisher, mainAccount);
        initReservationManager(contractPublisher, mainAccount);
    }

    private void initCoinManager(ContractPublisher contractPublisher, EthAccount mainAccount){
        try {
            ContractPublisher.Contract<Coin> mainContract = contractPublisher.compileAndPublish("coin.sol", "Coin", mainAccount ,Coin.class);
            coinManager = new CoinManager(ethereum, mainContract);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initReservationManager(ContractPublisher contractPublisher, EthAccount mainAccount){
        try {
            ContractPublisher.Contract<Reservation> mainContract = contractPublisher.compileAndPublish("reservation.sol", "Reservation", mainAccount ,Reservation.class);
            reservationManager = new ReservationManager(ethereum, mainContract);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    void createStartingAccounts(){
        accounts.put("bob",AccountProvider.from("bob"));
        accounts.put("alice", AccountProvider.from("alice"));
        accounts.put("main", AccountProvider.from("main"));
    }

    TestConfig createTestAccountsConfig(){
        return addEtherToMain(addEtherToAll(TestConfig.builder(), ether(100000)), ether(500000)).build();
    }

    public EthAccount addToAccounts(String name) throws Exception {
        if(accounts.containsKey(name)) throw new Exception("Account with the specified username already exists!");
        EthAccount account = AccountProvider.from(name);
        accounts.put(name, account);
        return account;
    }

    private TestConfig.Builder addEtherToMain(TestConfig.Builder configBuilder, EthValue ethValue){
        EthAccount mainAccount = accounts.get("main");
        configBuilder.balance(mainAccount, ethValue);
        return configBuilder;
    }

    private TestConfig.Builder addEtherToAll(TestConfig.Builder configBuilder, EthValue etherValue){
        for(EthAccount account : accounts.values()){
            configBuilder.balance(account, etherValue);
        }
        return configBuilder;
    }


    public void printEthereumBalances() {
        for(String name: accounts.keySet()){
            printUserEthereumBalance(name);
        }
    }

    public void printUserEthereumBalance(String name) {
        EthAccount account = accounts.get(name);
        if(account == null) {System.out.println("There is no account with name: " + name); return;}
        EthValue accountBalance = ethereum.getBalance(account);
        System.out.println("Balance of " + name + " \n\tin Ether: " + accountBalance.inEth() + "\n\tin Wei: " + accountBalance.inWei());

    }


    public void printCustomCurrencyBalances() {
        for(String name: accounts.keySet()){
            printUserCustomCurrencyBalance(name);
        }
    }


    public void printUserCustomCurrencyBalance(String name)  {
        EthAccount account = accounts.get(name);
        if(account == null) {System.out.println("There is no account with name: " + name); return;}
        coinManager.printUserCustomCurrencyBalance(account, name);
    }


    public Coin getCoinForName(String name){
        EthAccount account = accounts.get(name);
        if(account == null) {System.out.println("There is no account with name: " + name); return null;}
        return coinManager.getCoinForName(account, name);
    }

    public EthAccount getAccount(String name){
        return accounts.get(name);
    }

    public void printAccounts()  {
        for(String name : accounts.keySet()){
            printAccount(name);
        }
    }

    public void printAccount(String name) {
        EthAccount account = accounts.get(name);
        if(account == null) {System.out.println("There is no account with name: " + name); return;}
        System.out.println(name + "'s account: " + account.getAddress().toString());
    }
}

package extra;

import cli.MainCLI;
import interfaces.Coin;
import ethereum.AccountsManager;
import org.adridadou.ethereum.EthereumFacade;
import org.adridadou.ethereum.provider.EthereumFacadeProvider;

import org.adridadou.ethereum.provider.EthereumJConfigs;
import org.adridadou.ethereum.values.*;
import rx.Observable;

import java.io.File;

import static org.adridadou.ethereum.values.EthValue.ether;


public class CustomTest {

    private static boolean DEVEL_PHASE = false;

    private static EventHandler<Minted> mintedEventHandler = new EventHandler<>();
    private static EventHandler<Sent> sentEventHandler = new EventHandler<>();

    private static final AccountsManager accountsManager = new AccountsManager();
    private static final EthereumFacade ethereum = forTest();

    public static  void main(String[] args) throws Exception{
        new MainCLI(ethereum, accountsManager).mainLoop();
    }


    private static EthereumFacade forTest(){
        DEVEL_PHASE = true;
        accountsManager.createStartingAccounts();
        EthereumFacade ethereum = EthereumFacadeProvider.forTest(accountsManager.createTestAccountsConfig());
        accountsManager.setEthereum(ethereum);
        return ethereum;
    }

    private static EthereumFacade forNetwork(){
        DEVEL_PHASE = false;
        accountsManager.createStartingAccounts();
        EthereumFacade ethereum = EthereumFacadeProvider.forNetwork(EthereumJConfigs.etherCampTestnet()).create();
        accountsManager.setEthereum(ethereum);
        return ethereum;
    }

    private static void stop(){
        ethereum.shutdown();
        System.exit(0);
    }

    private static void coinContractTest() throws Exception{
        EthAccount eveAccount = accountsManager.addToAccounts("eve");

        accountsManager.printAccounts();
        accountsManager.printEthereumBalances();

        EthAccount bobAccount = accountsManager.getAccount("bob");
        EthAccount mainAccount = accountsManager.getAccount("main");
        EthAccount aliceAccount = accountsManager.getAccount("alice");

        Contract<Coin> mainContract = compileAndPublish("coin.sol", "Coin", Coin.class);
        Coin coin =  mainContract.proxy;

        Observable<Sent> sentEvent = ethereum.observeEvents(mainContract.compiledContract.getAbi(), mainContract.contractAddress, "Sent", Sent.class);
        Observable<Minted> mintedEvent = ethereum.observeEvents(mainContract.compiledContract.getAbi(), mainContract.contractAddress, "Minted", Minted.class);

        sentEvent.subscribe(sentEventHandler::handle);
        mintedEvent.subscribe(mintedEventHandler::handle);

        coin.mint(mainAccount, 1000).get();

        coin.mint(bobAccount, 100).get();
        coin.mint(aliceAccount, 100).get();
        coin.mint(eveAccount, 500).get();

        coin.send(aliceAccount, 50);

        Coin bobCoin = ethereum.createContractProxy(mainContract.compiledContract, mainContract.contractAddress, bobAccount, Coin.class);
        bobCoin.send(aliceAccount, 50).get();

        Coin aliceCoin = ethereum.createContractProxy(mainContract.compiledContract, mainContract.contractAddress, aliceAccount, Coin.class);
        aliceCoin.send(bobAccount, 25).get();

        Coin eveCoin = ethereum.createContractProxy(mainContract.compiledContract, mainContract.contractAddress, eveAccount, Coin.class);
        eveCoin.send(aliceAccount, 200).get();

        coin.mint(bobAccount, 10000).get();
        bobCoin.send(aliceAccount, 1000).get();
        ethereum.sendEther(bobAccount, aliceAccount.getAddress(), ether(10)).get();

        accountsManager.printCustomCurrencyBalances();
        accountsManager.printEthereumBalances();
    }

    private static <T> Contract<T> compileAndPublish(String contractFileName, String contractName, Class<T> interfaceClass)throws Exception{
        EthAccount mainAccount = accountsManager.getAccount("main");
        Contract<T> contract = new Contract<>();
        contract.contractSrc = SoliditySource.from(new File("contracts/" + contractFileName));
        contract.compiledContract = ethereum.compile(contract.contractSrc, contractName).get();
        contract.contractAddress = ethereum.publishContract(contract.compiledContract, mainAccount).get();
        contract.proxy =  ethereum.createContractProxy(contract.compiledContract, contract.contractAddress, mainAccount,interfaceClass);
        return contract;
    }

    private static class Contract <T>{
        SoliditySource contractSrc;
        CompiledContract compiledContract;
        EthAddress contractAddress;
        T proxy;
    }

    public static class Sent{

        private String from, to;
        private int amount;

        public Sent(String from, String to, int amount){
            this.from = from;
            this.to = to;
            this.amount = amount;
        }


        @Override public String toString(){
            return "Sent\n\tfrom : " + from + "\n\tto: " + to + "\n\tamount: " + amount;
        }
    }

    public static class Minted{
        private String receiver;
        private int amount;

        public  Minted(String receiver, int amount){
            this.receiver = receiver;
            this.amount = amount;
        }


        @Override public String toString(){
            return "Minted\n\taccount : " + receiver  + "\n\tamount: " + amount;
        }
    }

    private static class EventHandler <T>{
        private int i = 0;
        void handle(T event){
            if(DEVEL_PHASE){
                if(i == 0) {
                    System.out.println(event);
                    i = 1;
                }
                else i = 0;

            }
            else{
                System.out.println(event);
            }
        }
    }

}
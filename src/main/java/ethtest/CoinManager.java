package ethtest;

import org.adridadou.ethereum.EthereumFacade;
import org.adridadou.ethereum.values.EthAccount;
import rx.Observable;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

class CoinManager {

    private EthereumFacade ethereum;

    private ContractPublisher.Contract<Coin> mainContract;
    private HashMap<String, Coin> coins = new HashMap<>();

    CoinManager(EthereumFacade ethereum, ContractPublisher.Contract<Coin> mainContract, AccountsManager accountsManager){
        this.ethereum = ethereum;
        this.mainContract = mainContract;
        observeEvents(accountsManager);
    }

    private void observeEvents(AccountsManager accountsManager){
        EventHandler<Minted> mintedEventHandler = new EventHandler<>(accountsManager);
        EventHandler<Sent> sentEventHandler = new EventHandler<>(accountsManager);
        Observable<Sent> sentEvent = ethereum.observeEvents(mainContract.compiledContract.getAbi(), mainContract.contractAddress, "Sent", Sent.class);
        Observable<Minted> mintedEvent = ethereum.observeEvents(mainContract.compiledContract.getAbi(), mainContract.contractAddress, "Minted", Minted.class);
        sentEvent.subscribe(sentEventHandler::handle);
        mintedEvent.subscribe(mintedEventHandler::handle);

    }

    void printUserCustomCurrencyBalance(EthAccount account, String name)  {
        Coin coin = getCoinForName(account, name);
        try {
            System.out.println("Balance of " + name + " in custom currency: " + coin.getBalance(account).get());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    Coin getCoinForName(EthAccount account, String name){

        Coin coin = coins.get(name);
        if(coin == null){
            coin = ethereum.createContractProxy(mainContract.compiledContract, mainContract.contractAddress, account, Coin.class);
            coins.put(name, coin);
        }
        return coin;
    }


    public static class Sent extends SolEvent{

        private String from, to;
        private int amount;
        private int pastFromBalance, currentFromBalance;
        private int pastToBalance, currentToBalance;

        public Sent(String from, String to, int amount, int pastFromBalance, int currentFromBalance, int pastToBalance, int currentToBalance){
            this.from = from;
            this.to = to;
            this.amount = amount;
            this.pastFromBalance = pastFromBalance;
            this.pastToBalance = pastToBalance;
            this.currentFromBalance = currentFromBalance;
            this.currentToBalance = currentToBalance;
        }


        @Override public String toString(){
            return "Sent\n\tfrom : " + from + " ( %1$s  )"  +
                    "\n\tto: " + to + " ( %2$s ) "  +
                    "\n\tamount: " + amount +
                    "\n\tprev %1$s's balance: " + pastFromBalance + "" +
                    "\n\tcurrent %1$s's balance: " + currentFromBalance +
                    "\n\tprev %2$s's balance: " + pastToBalance +
                    "\n\tcurrent %2$s's balance: " + currentToBalance;
        }

        @Override
        public List<String> addressesToTranslate() {
            return Arrays.asList(from, to);
        }
    }

    public static class Minted extends SolEvent{
        private String receiver;
        private int amount;

        public  Minted(String receiver, int amount){
            this.receiver = receiver;
            this.amount = amount;
        }


        @Override public String toString(){
            return "Minted" +
                    "\n\taccount : " + receiver  + " ( %s )" +
                    "\n\tamount: " + amount;
        }

        @Override
        public List<String> addressesToTranslate() {
            return Collections.singletonList(receiver);
        }
    }
}

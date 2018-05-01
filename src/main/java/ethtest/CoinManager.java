package ethtest;

import org.adridadou.ethereum.EthereumFacade;
import org.adridadou.ethereum.values.EthAccount;

import java.util.HashMap;
import java.util.concurrent.ExecutionException;

class CoinManager {

    private EthereumFacade ethereum;

    private ContractPublisher.Contract<Coin> mainContract;
    private HashMap<String, Coin> coins = new HashMap<>();

    CoinManager(EthereumFacade ethereum, ContractPublisher.Contract<Coin> mainContract){
        this.ethereum = ethereum;
        this.mainContract = mainContract;
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
}

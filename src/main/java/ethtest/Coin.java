package ethtest;

import org.adridadou.ethereum.values.EthAccount;
import org.adridadou.ethereum.values.Payable;

import java.util.concurrent.CompletableFuture;

public interface Coin {

    CompletableFuture<Void> mint(EthAccount receiver, int amount);
    CompletableFuture<Void> send(EthAccount receiver, int amount);
    CompletableFuture<Integer> getBalance (EthAccount account);
}

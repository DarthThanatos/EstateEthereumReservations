package interfaces;

import org.adridadou.ethereum.values.EthAccount;

import java.util.concurrent.CompletableFuture;

public interface Coin {

    CompletableFuture<Void> mint(EthAccount receiver, int amount);
    CompletableFuture<Void> send(EthAccount receiver, int amount);
    CompletableFuture<Integer> getBalance (EthAccount account);
}

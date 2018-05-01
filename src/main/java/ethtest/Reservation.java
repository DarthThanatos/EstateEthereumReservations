package ethtest;

import java.util.concurrent.CompletableFuture;

public interface Reservation {
    CompletableFuture<Void> tryToReserve(int day);
}

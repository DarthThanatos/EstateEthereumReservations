package ethtest;

import org.adridadou.ethereum.values.EthAccount;

import java.util.concurrent.CompletableFuture;

public interface Reservations {

    CompletableFuture<Void> tryToReserve(EthAccount client, int day);
    CompletableFuture<Void> publishEstate(String name, int price, Boolean[] daysAvailabilityStates, Boolean[] daysReservationStates);
    ReservationManager.Estate getEstatesByOwner(EthAccount estatesOwner, int index);

}

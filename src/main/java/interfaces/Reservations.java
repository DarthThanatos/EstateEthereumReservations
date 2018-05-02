package interfaces;

import ethereum.ReservationManager;
import org.adridadou.ethereum.values.EthAccount;

import java.util.concurrent.CompletableFuture;

public interface Reservations {

    CompletableFuture<Void> tryToReserve(EthAccount estateOwner, int estateIndex, int day);
    CompletableFuture<Void> tryToCancel(EthAccount estateOwner, int estateIndex, int day);

    CompletableFuture<Void> publishEstate(String name, int price, Boolean[] daysAvailabilityStates, Boolean[] daysReservationStates);
    ReservationManager.Estate getEstateOfOwnerByIndex(EthAccount estatesOwner, int index);
    ReservationManager.Estate getEstateByIndex(int index);
    int getOwnerEstatesAmount(EthAccount estatesOwner);
    int getAllEstatesAmount();

}

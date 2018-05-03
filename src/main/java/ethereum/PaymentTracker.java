package ethereum;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

import java.util.HashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;


class PaymentTracker {

    private AccountsManager accountsManager;

    PaymentTracker(AccountsManager accountsManager){
        this.accountsManager = accountsManager;
    }

    class ReservationDetails{
        final String owner;
        final int estateIndex;
        final int day;

        ReservationDetails(String owner, int estateIndex, int day){
            this.owner = owner;
            this.estateIndex = estateIndex;
            this.day = day;
        }
        public int hashCode(){
            int hashcode;
            hashcode = day * 20;
            hashcode += estateIndex * 40;
            hashcode += owner.hashCode();
            return hashcode;
        }

        public boolean equals(Object obj){
            if (obj instanceof ReservationDetails) {
                ReservationDetails pp = (ReservationDetails) obj;
                return (pp.owner.equals(this.owner) && pp.estateIndex == this.estateIndex && pp.day == this.day);
            } else {
                return false;
            }
        }
    }

    private HashMap<ReservationDetails, Boolean> reservationActiveMap = new HashMap<>();


    void handleReservation(ReservationManager.ReservationMade reservationMade){
        ReservationDetails reservationDetails = new ReservationDetails(reservationMade.estateOwnerAddressString, reservationMade.estateIndex, reservationMade.day);
        reservationActiveMap.put(reservationDetails, true);
        Observable.interval(20, TimeUnit.SECONDS).subscribeOn(Schedulers.newThread()).forEachWhile(
                aLong -> shouldStopObservingReservation(aLong, reservationDetails),
                Throwable::printStackTrace
        );
    }

    @SuppressWarnings("unused")
    private boolean shouldStopObservingReservation(long l, ReservationDetails reservationDetails){

        System.out.println("checking if reservation is active and paid: account: "
                + accountsManager.getReadableNameFromHexForm(reservationDetails.owner)
                + " estate index: " + reservationDetails.estateIndex
                + " day: " + reservationDetails.day
        );

        boolean paid = false;
        try {
            paid = accountsManager.getReservationsForName("main")
                    .checkPaidForReservation(
                            accountsManager.getAcountFromHex(reservationDetails.owner),
                            reservationDetails.estateIndex,
                            reservationDetails.day
                    ).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        boolean reservationActive = reservationActiveMap.get(reservationDetails);
        return reservationActive && paid; //still observes if it gives true; stops observing if at least one condition fails
    }

    void handleCancel(ReservationManager.ReservationCanceled reservationCanceled){
        reservationActiveMap.put(new ReservationDetails(reservationCanceled.estateOwnerAddressString, reservationCanceled.estateIndex, reservationCanceled.day), false);
    }

}


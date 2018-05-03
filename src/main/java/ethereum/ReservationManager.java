package ethereum;

import event.EventHandler;
import event.SolEvent;
import interfaces.Reservations;
import org.adridadou.ethereum.EthereumFacade;
import org.adridadou.ethereum.values.EthAccount;
import org.adridadou.ethereum.values.EthAddress;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;
public class ReservationManager {

    private EthereumFacade ethereum;

    private ContractPublisher.Contract<Reservations> mainContract;
    private HashMap<String, Reservations> reservations = new HashMap<>();

    private PaymentTracker paymentTracker;

    ReservationManager(EthereumFacade ethereum, ContractPublisher.Contract<Reservations> mainContract, AccountsManager accountsManager){
        this.ethereum = ethereum;
        this.mainContract = mainContract;
        this.paymentTracker = new PaymentTracker(accountsManager);
        observeEvents(accountsManager);
    }


    EthAddress getReservationsContractAddr(){
        return mainContract.contractAddress;
    }

    private void observeEvents(AccountsManager accountsManager){
        observeEvent(accountsManager, "PublishedEstate", PublishedEstate.class);
        observeEvent(accountsManager, "ReservationMade", ReservationMade.class, paymentTracker::handleReservation);
        observeEvent(accountsManager, "ReservationCanceled", ReservationCanceled.class, paymentTracker::handleCancel);
    }



    @SuppressWarnings("SameParameterValue")
    private <T extends SolEvent> void observeEvent(AccountsManager accountsManager, String eventName, Class<T> eventClass){
        observeEvent(accountsManager, eventName, eventClass, null);
    }

    private <T extends SolEvent> void observeEvent(AccountsManager accountsManager, String eventName, Class<T> eventClass, EventHandler.NonDefaultEventHandler<T> nonDefaultEventHandler){
        EventHandler<T> eventHandler = new EventHandler<>(accountsManager);
        rx.Observable<T> event = ethereum.observeEvents(mainContract.compiledContract.getAbi(), mainContract.contractAddress, eventName, eventClass);
        event.subscribe(
                e -> eventHandler.handle(e, nonDefaultEventHandler),
                Throwable::printStackTrace
        );

    }

    Reservations getReservationForName(EthAccount account, String name){
        Reservations reservation = reservations.get(name);
        if(reservation == null){
            reservation = ethereum.createContractProxy(mainContract.compiledContract, mainContract.contractAddress, account, Reservations.class);
            reservations.put(name, reservation);
        }
        return reservation;
    }

    public static class Estate{
        private String estateOwnerHexString;
        private String name;
        private Integer price;
        private Boolean[] daysAvailabilityStates;
        private Boolean[] daysReservationStates;

        public Estate(String estateOwnerHexString, String name, Integer price, Boolean[] daysAvailabilityStates, Boolean[] daysReservationStates){
            this.estateOwnerHexString = estateOwnerHexString;
            this.name = name;
            this.price = price;
            this.daysAvailabilityStates = daysAvailabilityStates;
            this.daysReservationStates = daysReservationStates;
        }

        @Override public String toString(){
            return "Estate \n\towner: " + estateOwnerHexString + " ( %s )" +
                    "\n\tname: " + name +
                    "\n\tprice: " + price +
                    "\n\tDays available for making reservations: " + getReadableDays(daysAvailabilityStates, "No available days for making reservations.");
        }

        public static void printEstateWithTenantInfo(Reservations reservationForName, Estate estate,  EthAccount owner, int index, AccountsManager accountsManager){
            System.out.print(String.format(estate.toString(), accountsManager.getReadableNameFromHexForm(estate.estateOwnerHexString)));
            System.out.println(reservedWithTenants(reservationForName, estate,  owner, index, accountsManager));
        }

        public static void printEstateWithTenantInfo(Reservations reservationForName, Estate estate, int index, AccountsManager accountsManager){
            System.out.print(String.format(estate.toString(), accountsManager.getReadableNameFromHexForm(estate.estateOwnerHexString)));
            System.out.println(reservedWithTenants(reservationForName, estate,  null, index, accountsManager));
        }

        private static String reservedWithTenants(Reservations reservations, Estate estate,  EthAccount owner, int index, AccountsManager accountsManager){
            String res ="\n\tDays already reserved: ";
            String[] days = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
            StringBuilder stringBuilder = new StringBuilder();
            boolean atLeastOneTrue = false;
            for (int i = 0; i < 7; i++) {
                if (estate.getDaysReservationStates()[i]) {
                    try {
                        stringBuilder.append(days[i]).append(":").append(
                                owner != null
                                ? accountsManager.getReadableNameFromHexForm(reservations.getTenantOfOwner(owner, index, i))
                                : accountsManager.getReadableNameFromHexForm(reservations.getTenant(index, i))
                        ).append(" [toPay=").append(
                                owner != null
                                        ? reservations.getRemainingQuote(owner, index, i).get()
                                        : reservations.getRemainingQuoteGlobal(index, i).get()

                        ).append("]");
                    } catch (InterruptedException | ExecutionException e) {
                        e.printStackTrace();
                    }
                    atLeastOneTrue = true;
                }
            }
            return atLeastOneTrue ? res + stringBuilder.toString() :  "\n\tNo reservations made so far";
        }

        @SuppressWarnings("SameParameterValue")
        private String getReadableDays(Boolean[] dayStates, String defaultTxt) {
            String[] days = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
            StringBuilder res = new StringBuilder();
            boolean atLeastOneTrue = false;
            for (int i = 0; i < 7; i++) {
                if (dayStates[i]) {
                    res.append(days[i]).append(" ");
                    atLeastOneTrue = true;
                }
            }
            return atLeastOneTrue ? res.toString() : defaultTxt;
        }

        Boolean[] getDaysReservationStates() {
            return daysReservationStates;
        }
    }

    public static class ReservationMade extends SolEvent{

        final String estateOwnerAddressString;
        final int estateIndex;
        final String name;
        final String clientAddrString;
        final int day;

        public ReservationMade(String estateOwnerAddressString, int estateIndex, String name, String clientAddrString, int day){
            this.estateOwnerAddressString = estateOwnerAddressString;
            this.estateIndex = estateIndex;
            this.name = name;
            this.clientAddrString = clientAddrString;
            this.day = day;
        }


        @Override public String toString(){
            String[] days = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
            return "Reservation was made" +
                    "\n\towner: " + estateOwnerAddressString + " ( %s ) " +
                    "\n\testate index: " + estateIndex +
                    "\n\testate name: " + name +
                    "\n\ttenant: " + clientAddrString + " ( %s ) " +
                    "\n\tday: " + days[day];
        }

        @Override
        public List<String> addressesToTranslate() {
            return Arrays.asList(estateOwnerAddressString, clientAddrString);
        }
    }

    public static class ReservationCanceled extends SolEvent{

        final String estateOwnerAddressString;
        final int estateIndex;
        final  String name;
        final  String clientAddrString;
        final int day;

        public ReservationCanceled(String estateOwnerAddressString, int estateIndex, String name, String clientAddrString, int day){
            this.estateOwnerAddressString = estateOwnerAddressString;
            this.estateIndex = estateIndex;
            this.name = name;
            this.clientAddrString = clientAddrString;
            this.day = day;
        }

        @Override public String toString(){
            String[] days = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
            return "Reservation was canceled" +
                    "\n\towner: " + estateOwnerAddressString + " ( %s ) " +
                    "\n\testate index: " + estateIndex +
                    "\n\testate name: " + name +
                    "\n\ttenant: " + clientAddrString + " ( %s ) " +
                    "\n\tday: " + days[day];
        }

        @Override
        public List<String> addressesToTranslate() {
            return Arrays.asList(estateOwnerAddressString, clientAddrString);
        }
    }

    public static class PublishedEstate extends SolEvent {
        final  String estatesOwnerAddressString;
        final  String name;
        final  int price;
        final  Boolean[]daysAvailabilityStates;

        public  PublishedEstate(String estatesOwnerAddressString, String name, int price, Boolean[] daysAvailabilityStates){
            this.estatesOwnerAddressString = estatesOwnerAddressString;
            this.name = name;
            this.price = price;
            this.daysAvailabilityStates = daysAvailabilityStates;
        }


        @Override public String toString(){
            return "Published estate: " +
                    "\n\towner: " + estatesOwnerAddressString + " ( %s ) " +
                    "\n\tname of estate: " + name +
                    "\n\tprice of reserving: " + price +
                    "\n\tavailable: " + getReadableAvailableDays();
        }

        private String getReadableAvailableDays(){
            String[] days = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
            StringBuilder res = new StringBuilder();
            boolean atLeastOneTrue = false;
            for(int i = 0; i < 7; i++){
                if(daysAvailabilityStates[i]) {
                    res.append(days[i]).append(" ");
                    atLeastOneTrue = true;
                }
            }
            return atLeastOneTrue ? res.toString() : "No available days for making reservations.";
        }

        @Override
        public List<String> addressesToTranslate() {
            return Collections.singletonList(estatesOwnerAddressString);
        }
    }

}


package ethereum;

import event.EventHandler;
import event.SolEvent;
import interfaces.Reservations;
import org.adridadou.ethereum.EthereumFacade;
import org.adridadou.ethereum.values.EthAccount;
import rx.Observable;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class ReservationManager {

    private EthereumFacade ethereum;

    private ContractPublisher.Contract<Reservations> mainContract;
    private HashMap<String, Reservations> reservations = new HashMap<>();

    ReservationManager(EthereumFacade ethereum, ContractPublisher.Contract<Reservations> mainContract, AccountsManager accountsManager){
        this.ethereum = ethereum;
        this.mainContract = mainContract;
        observeEvents(accountsManager);
    }

    private void observeEvents(AccountsManager accountsManager){
        observeEvent(accountsManager, "PublishedEstate", PublishedEstate.class);
        observeEvent(accountsManager, "ReservationMade", ReservationMade.class);
        observeEvent(accountsManager, "ReservationCanceled", ReservationCanceled.class);
    }

    private <T extends SolEvent> void observeEvent(AccountsManager accountsManager, String eventName, Class<T> eventClass){
        EventHandler<T> eventHandler = new EventHandler<>(accountsManager);
        Observable<T> event =
                ethereum.observeEvents(mainContract.compiledContract.getAbi(), mainContract.contractAddress, eventName, eventClass);
        event.subscribe(eventHandler::handle, Throwable::printStackTrace);

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
            return "Estate \n\towner: " + estateOwnerHexString + "\n\tname: " + name + "\n\tprice: " + price;
        }
    }

    public static class ReservationMade extends SolEvent{

        private String estateOwnerAddressString;
        private int estateIndex;
        private String name;
        private String clientAddrString;
        private int day;

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

        private String estateOwnerAddressString;
        private int estateIndex;
        private String name;
        private String clientAddrString;
        private int day;

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
        private String estatesOwnerAddressString;
        private String name;
        private int price;
        private Boolean[]daysAvailabilityStates;

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
            for(int i = 0; i < 7; i++){
                if(daysAvailabilityStates[i]) res.append(days[i]).append(" ");
            }
            return res.toString();
        }

        @Override
        public List<String> addressesToTranslate() {
            return Collections.singletonList(estatesOwnerAddressString);
        }
    }
}

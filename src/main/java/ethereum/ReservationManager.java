package ethereum;

import event.EventHandler;
import event.SolEvent;
import interfaces.Reservations;
import org.adridadou.ethereum.EthereumFacade;
import org.adridadou.ethereum.values.EthAccount;
import rx.Observable;

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
        EventHandler<PublishedEstate> publishedEstateEventHandler = new EventHandler<>(accountsManager);
        Observable<PublishedEstate> publishedEstateEvent =
                ethereum.observeEvents(mainContract.compiledContract.getAbi(), mainContract.contractAddress, "PublishedEstate", PublishedEstate.class);
        publishedEstateEvent.subscribe(publishedEstateEventHandler::handle, Throwable::printStackTrace);

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

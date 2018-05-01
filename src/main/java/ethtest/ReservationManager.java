package ethtest;

import org.adridadou.ethereum.EthereumFacade;
import org.adridadou.ethereum.values.EthAccount;

import java.util.HashMap;

class ReservationManager {

    private EthereumFacade ethereum;

    private ContractPublisher.Contract<Reservations> mainContract;
    private HashMap<String, Reservations> reservations = new HashMap<>();

    ReservationManager(EthereumFacade ethereum, ContractPublisher.Contract<Reservations> mainContract){
        this.ethereum = ethereum;
        this.mainContract = mainContract;
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
}

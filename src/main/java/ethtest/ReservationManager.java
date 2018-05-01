package ethtest;

import org.adridadou.ethereum.EthereumFacade;
import org.adridadou.ethereum.values.EthAccount;

import java.util.HashMap;

class ReservationManager {

    private EthereumFacade ethereum;

    private ContractPublisher.Contract<Reservation> mainContract;
    private HashMap<String, Reservation> reservations = new HashMap<>();

    ReservationManager(EthereumFacade ethereum, ContractPublisher.Contract<Reservation> mainContract){
        this.ethereum = ethereum;
        this.mainContract = mainContract;
    }


    Reservation getReservationForName(EthAccount account, String name){

        Reservation reservation = reservations.get(name);
        if(reservation == null){
            reservation = ethereum.createContractProxy(mainContract.compiledContract, mainContract.contractAddress, account, Reservation.class);
            reservations.put(name, reservation);
        }
        return reservation;
    }
}

package commands;

import cli.UserCLI;
import ethereum.AccountsManager;
import interfaces.Reservations;
import org.adridadou.ethereum.values.EthAccount;

public class MakeReservationCommand extends ReservationCommand {

    public MakeReservationCommand(AccountsManager accountsManager, UserCLI userCLI) {
        super(accountsManager, userCLI);
    }

    @Override
    void makeReservationAction(EthAccount estateOwnerAccount, int estateIndex, int day, Reservations reservationForName) throws Exception {
        reservationForName.tryToReserve(estateOwnerAccount, estateIndex, day).get();
    }

    @Override
    public String getDescription() {
        return "Tries to make a reservation given an estate owner and id of the estate and the day (0 -> Monday ... 6 -> Sunday).";
    }


}

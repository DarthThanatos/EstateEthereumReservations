package commands;

import cli.UserCLI;
import ethereum.AccountsManager;
import interfaces.Reservations;
import org.adridadou.ethereum.values.EthAccount;

public class CancelReservationCommand extends ReservationCommand {

    public CancelReservationCommand(AccountsManager accountsManager, UserCLI userCLI) {
        super(accountsManager, userCLI);
    }

    @Override
    public String getDescription() {
        return "Tries to cancel a reservation given an estate owner and id of the estate and the day (0 -> Monday ... 6 -> Sunday).";
    }

    @Override
    void makeReservationAction(EthAccount account, int index, int day, Reservations reservationsForName) throws Exception {
        reservationsForName.tryToCancel(account, index, day);
    }
}

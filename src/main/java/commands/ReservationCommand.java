package commands;

import cli.UserCLI;
import ethereum.AccountsManager;
import interfaces.Reservations;
import org.adridadou.ethereum.values.EthAccount;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public abstract class ReservationCommand extends CLICommand{

    AccountsManager accountsManager;
    private UserCLI userCLI;

    ReservationCommand(AccountsManager accountsManager, UserCLI userCLI, String cmdName){
        super(cmdName);
        this.accountsManager = accountsManager;
        this.userCLI = userCLI;
    }

    abstract void makeReservationAction(EthAccount account, int index, int day, Reservations reservationsForName) throws Exception;

    @Override
    public void execute() {

        EthAccount account;
        int index;
        int day;

        if(argcCorrect(3) && (account = getAccount(accountsManager, 0)) != null && (index = getInt(1))!= -1 && (day = getDay(2))!= -1){
            onArgumentsCorrect(account, index, day);
        }
    }

    @Override
    public List<String> getRequiredArgumentsList() {
        return Arrays.asList("estateOwner", "estateIndex", "day");
    }

    @Override
    public List<String> getOptionalArgumentsList() {
        return new ArrayList<>();
    }


    private void onArgumentsCorrect(EthAccount account, int index, int day){
        Reservations reservationForName = accountsManager.getReservationsForName(userCLI.getUserName());
        try{
            makeReservationAction(account, index, day, reservationForName);
        }
        catch(Exception e){
            System.out.println("No estate having given properties");
        }
    }

}

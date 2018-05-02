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

    ReservationCommand(AccountsManager accountsManager, UserCLI userCLI){
        this.accountsManager = accountsManager;
        this.userCLI = userCLI;
    }

    abstract void makeReservationAction(EthAccount account, int index, int day, Reservations reservationsForName) throws Exception;

    @Override
    public void execute() {

        EthAccount account;
        int index;
        int day;

        if(argcCorrect() && (account = getAccount()) != null && (index = getIndex())!= -1 && (day = getDay())!= -1){
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

    private boolean argcCorrect(){
        if(parsedCommandLine.args.size() < 3){
            System.out.println("Invalid usage, check help command for details");
            return false;
        }
        return true;
    }

    private EthAccount getAccount(){
        String estateOwnerName = parsedCommandLine.args.get(0);
        EthAccount account = accountsManager.getAccount(estateOwnerName);
        if(account == null){
            System.out.println("There is no user with such a name");
        }
        return  account;
    }

    private int getIndex(){
        return getInt(1);
    }

    private int getDay(){
        int day = getInt(2);
        if(day < 0 || day > 6){
            System.out.println("Day needs to be in range [0,6]");
            return -1;
        }
        return day;
    }

    private int getInt(int argIndex){
        try{
            return Integer.parseInt(parsedCommandLine.args.get(argIndex));
        }catch(NumberFormatException e){
            System.out.println("You need to specify a valid integer at " + argIndex);
            return -1;
        }
    }
}

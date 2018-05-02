package commands;

import cli.UserCLI;
import ethereum.AccountsManager;
import interfaces.Reservations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class ChangeAvailabilityStatusCommand extends CLICommand {

    private AccountsManager accountsManager;
    private UserCLI userCLI;


    public ChangeAvailabilityStatusCommand(AccountsManager accountsManager, UserCLI userCLI){
        this.accountsManager = accountsManager;
        this.userCLI = userCLI;
    }

    @Override
    public void execute() {
        int day, estateIndex;
        if(argcCorrect() && (estateIndex = getEstateIndex()) != -1 &&(day = getDay()) != -1){
            boolean isAvailable = isAvailable();
            Reservations reservations = accountsManager.getReservationsForName(userCLI.getUserName());
            tryToChangeAvailabilityStatus(reservations, estateIndex, day, isAvailable);
        }
    }

    private void tryToChangeAvailabilityStatus(Reservations reservations, int estateIndex, int day, boolean isAvailable){
        try{
            reservations.tryToChangeAvailableDay(estateIndex, day, isAvailable).get();
        }catch(Exception e){
            System.out.println(e.getMessage());
            System.out.println("No estate with the given properties");
        }
    }

    @Override
    public List<String> getRequiredArgumentsList() {
        return Arrays.asList("estateLocalIndex", "day", "t|f");
    }

    @Override
    public List<String> getOptionalArgumentsList() {
        return new ArrayList<>();
    }

    @Override
    public String getDescription() {
        return "Changes availability status of a given day of a given estate.";
    }

    private boolean argcCorrect(){
        if(parsedCommandLine.args.size() < 3){
            System.out.println("Invalid usage, check help command for details");
            return false;
        }
        return true;
    }


    private int getDay(){
        int day = getInt(1);
        if(day < 0 || day > 6){
            System.out.println("Day needs to be in range [0,6]");
            return -1;
        }
        return day;
    }

    private int getInt(int index){
        try{
            return Integer.parseInt(parsedCommandLine.args.get(index));
        }catch(NumberFormatException e){
            System.out.println("You need to specify a valid integer at " + index);
            return -1;
        }
    }

    private boolean isAvailable(){
        int flagIndex = 2;
        return Objects.equals(parsedCommandLine.args.get(flagIndex), "t");
    }

    private int getEstateIndex(){
        return getInt(0);
    }
}

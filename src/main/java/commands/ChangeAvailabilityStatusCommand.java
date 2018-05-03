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


    public ChangeAvailabilityStatusCommand(AccountsManager accountsManager, UserCLI userCLI, String userName){
        super(userName);
        this.accountsManager = accountsManager;
        this.userCLI = userCLI;
    }

    @Override
    public void execute() {
        int day, estateIndex;
        if(argcCorrect(3) && (estateIndex = getInt(0)) != -1 &&(day = getDay(1)) != -1){
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

    private boolean isAvailable(){
        int flagIndex = 2;
        return Objects.equals(parsedCommandLine.args.get(flagIndex), "t");
    }

}

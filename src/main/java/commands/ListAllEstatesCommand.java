package commands;

import ethereum.AccountsManager;
import ethereum.ReservationManager;
import interfaces.Reservations;
import cli.UserCLI;
import org.adridadou.ethereum.values.EthAccount;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ListAllEstatesCommand extends CLICommand {

    private AccountsManager accountsManager;
    private UserCLI userCLI;

    public ListAllEstatesCommand(AccountsManager accountsManager, UserCLI userCLI, String cmdName){
        super(cmdName);
        this.accountsManager = accountsManager;
        this.userCLI = userCLI;
    }

    @Override
    public void execute() {
        Reservations reservationsForName = accountsManager.getReservationsForName(userCLI.getUserName());
        printEstates(reservationsForName);
    }

    @Override
    public List<String> getRequiredArgumentsList() {
        return new ArrayList<>();
    }

    @Override
    public List<String> getOptionalArgumentsList() {
        return Collections.singletonList("owner");
    }

    @Override
    public String getDescription() {
        return "Prints all estates of an owner if given his name. Prints all estates of all owners if not given any arguments.";
    }

    private void printEstates( Reservations reservationsForName ){
        if(parsedCommandLine.args.size() == 0)
            printAllEstates(reservationsForName);
        else{
            printAllEstatesOfOwner(reservationsForName);
        }
    }

    private void printAllEstates(Reservations reservationsForName){
        int maxEstates = getMaxEstates(reservationsForName);
        for(int i = 0; i < maxEstates; i++) printEstate(reservationsForName, i);

    }

    @SuppressWarnings("EmptyCatchBlock")
    private void printEstate(Reservations reservationsForName, int i){
        try {
            ReservationManager.Estate.printEstateWithTenantInfo(reservationsForName, reservationsForName.getEstateByIndex(i), i, accountsManager);
        }catch(Exception e){ }
    }


    @SuppressWarnings("EmptyCatchBlock")
    private int getMaxEstates(Reservations reservationsForName){
        int maxEstates = 0;
        try {
            maxEstates = reservationsForName.getAllEstatesAmount();
        }catch(Exception e){}
        return maxEstates;
    }

    private void printAllEstatesOfOwner(Reservations reservationsForName){
        EthAccount account;
        if((account = getAccount(accountsManager, 0)) != null){
            int maxEstates = getMaxEstatesOfAccount(reservationsForName, account);
            for(int i = 0; i < maxEstates; i++) printEstateOfOwner(reservationsForName, account, i);
        }
    }

    @SuppressWarnings("EmptyCatchBlock")
    private void printEstateOfOwner(Reservations reservationsForName, EthAccount account, int i){
        try {
            ReservationManager.Estate.printEstateWithTenantInfo(reservationsForName, reservationsForName.getEstateOfOwnerByIndex(account, i), account, i, accountsManager);
        }catch(Exception e){ }
    }

    @SuppressWarnings("EmptyCatchBlock")
    private int getMaxEstatesOfAccount(Reservations reservationsForName, EthAccount account){
        int maxEstates = 0;
        try {
            maxEstates = reservationsForName.getOwnerEstatesAmount(account);
        }catch(Exception e){}
        return maxEstates;
    }

}

package commands;

import ethereum.AccountsManager;
import ethereum.ReservationManager;
import interfaces.Reservations;
import cli.UserCLI;
import org.adridadou.ethereum.values.EthAccount;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PrintEstateCommand extends CLICommand {

    private AccountsManager accountsManager;
    private UserCLI userCLI;

    public PrintEstateCommand(AccountsManager accountsManager, UserCLI userCLI, String cmdName){
        super(cmdName);
        this.accountsManager = accountsManager;
        this.userCLI = userCLI;
    }

    @Override
    public void execute() {
        EthAccount account;
        int index;

        if(argcCorrect(2)  && ((account = getAccount(accountsManager,0)) != null) && ((index = getInt(1)) != -1)){
            printEstateOfOwnerAt(account, index);
        }
    }

    private void printEstateOfOwnerAt(EthAccount account, int index){
        Reservations reservationsForName = accountsManager.getReservationsForName(userCLI.getUserName());
        try {
            ReservationManager.Estate.printEstateWithTenantInfo(reservationsForName, reservationsForName.getEstateOfOwnerByIndex(account, index), account, index, accountsManager);
        }catch(Exception e){
            System.out.println("No estate with specified parameters found");
        }
    }

    @Override
    public List<String> getRequiredArgumentsList() {
        return Arrays.asList("ownerName","index");
    }

    @Override
    public List<String> getOptionalArgumentsList() {
        return new ArrayList<>();
    }

    @Override
    public String getDescription() {
        return "Prints details of an estate published by an owner with the given name and at the specified index";
    }
}

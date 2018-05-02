package commands;

import ethereum.AccountsManager;
import interfaces.Reservations;
import cli.UserCLI;
import org.adridadou.ethereum.values.EthAccount;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PrintEstateCommand extends CLICommand {

    private AccountsManager accountsManager;
    private UserCLI userCLI;

    public PrintEstateCommand(AccountsManager accountsManager, UserCLI userCLI){
        this.accountsManager = accountsManager;
        this.userCLI = userCLI;
    }

    @Override
    public void execute() {
        if(parsedCommandLine.args.size() < 2){
            System.out.println("Invalid usage, check help command for details");
            return;
        }

        String estateOwnerName = parsedCommandLine.args.get(0);
        EthAccount account = accountsManager.getAccount(estateOwnerName);
        if(account == null){
            System.out.println("There is no user with such a name");
            return;
        }

        int index;
        try{
            index = Integer.parseInt(parsedCommandLine.args.get(1));
        }catch(NumberFormatException e){
            System.out.println("You need to specify a valid amount");
            return;
        }

        Reservations reservationsForName = accountsManager.getReservationsForName(userCLI.getUserName());
        try {
            System.out.println(reservationsForName.getEstateOfOwnerByIndex(account, index));
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

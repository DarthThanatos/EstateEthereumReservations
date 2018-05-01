package ethtest.commands;

import ethtest.AccountsManager;
import ethtest.Reservations;
import ethtest.UserCLI;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class PublishEstateCommand extends CLICommand {

    private AccountsManager accountsManager;
    private UserCLI userCLI;

    public PublishEstateCommand(AccountsManager accountsManager, UserCLI userCLI){
        this.accountsManager = accountsManager;
        this.userCLI = userCLI;
    }

    @Override
    public void execute() {
        if(parsedCommandLine.args.size() < 2){
            System.out.println("Invalid usage, check help command for details");
            return;
        }

        String name = parsedCommandLine.args.get(0);
        int price;
        try{
            price = Integer.parseInt(parsedCommandLine.args.get(1));
        }catch(NumberFormatException e){
            System.out.println("You need to specify a valid amount");
            return;
        }

        Reservations reservations = accountsManager.getReservationsForName(userCLI.getUserName());
        try {
            reservations.publishEstate(name, price, booleanArrayOf(true), booleanArrayOf(false)).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

    }

    private Boolean[] booleanArrayOf(Boolean val){
        Boolean[] res = new Boolean[7];
        for (int i = 0; i < 7; i++){
            res[i] = val;
        }
        return res;
    }

    @Override
    public List<String> getRequiredArgumentsList() {
        return Arrays.asList("estateName","estatePrice");
    }

    @Override
    public List<String> getOptionalArgumentsList() {
        return new ArrayList<>();
    }

    @Override
    public String getDescription() {
        return "Publishes a new estate offer.";
    }
}

package commands;

import ethereum.AccountsManager;
import interfaces.Reservations;
import cli.UserCLI;

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
        int price;
        String estateName;
        if(argcCorrect() && (price = getPrice())!= -1){
            estateName = parsedCommandLine.args.get(0);
            publishEstate(estateName, price);
        }
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


    private void publishEstate(String name, int price){
        Reservations reservations = accountsManager.getReservationsForName(userCLI.getUserName());
        try {
            reservations.publishEstate(name, price, booleanArrayOf(true), booleanArrayOf(false)).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    private boolean argcCorrect(){
        if(parsedCommandLine.args.size() < 2){
            System.out.println("Invalid usage, check help command for details");
            return false;
        }
        return true;
    }

    private int getPrice(){
        try{
            return Integer.parseInt(parsedCommandLine.args.get(1));
        }catch(NumberFormatException e){
            System.out.println("You need to specify a valid amount");
            return -1;
        }
    }

    private Boolean[] booleanArrayOf(Boolean val){
        Boolean[] res = new Boolean[7];
        for (int i = 0; i < 7; i++){
            res[i] = val;
        }
        return res;
    }

}

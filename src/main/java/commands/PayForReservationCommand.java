package commands;

import cli.UserCLI;
import ethereum.AccountsManager;
import interfaces.Coin;
import org.adridadou.ethereum.values.EthAccount;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PayForReservationCommand extends CLICommand {

    private AccountsManager accountsManager;
    private UserCLI userCLI;

    public PayForReservationCommand(AccountsManager accountsManager, UserCLI userCLI){
        this.accountsManager = accountsManager;
        this.userCLI = userCLI;
    }

    @Override
    public void execute() {
        EthAccount account;
        int estateIndex, amount, day;
        if(argcCorrect() && (account = getAccount()) != null && (estateIndex = getEstateIndex())!= -1 && (amount = getAmount())!= -1 && (day = getDay())!= -1){
            Coin coin = accountsManager.getCoinForName(userCLI.getUserName());
            coin.payForReservation(account, estateIndex, amount, day, accountsManager.getReservationsAddr());
        }
    }

    @Override
    public List<String> getRequiredArgumentsList() {
        return Arrays.asList("estateOwner", "estateLocalIndex", "amount", "day");
    }

    @Override
    public List<String> getOptionalArgumentsList() {
        return new ArrayList<>();
    }

    @Override
    public String getDescription() {
        return "Pays for a specific day of as specific estate a specific amount of cryptocurrency";
    }

    private EthAccount getAccount(){
        String estateOwnerName = parsedCommandLine.args.get(0);
        EthAccount account = accountsManager.getAccount(estateOwnerName);
        if(account == null){
            System.out.println("There is no user with such a name");
        }
        return  account;
    }


    private boolean argcCorrect(){
        if(parsedCommandLine.args.size() < 4){
            System.out.println("Invalid usage, check help command for details");
            return false;
        }
        return true;
    }


    private int getAmount(){
        return getInt(2);
    }

    private int getDay(){
        int day = getInt(3);
        if(day < 0 || day > 6){
            System.out.println("Day needs to be in range [0,6]");
            return -1;
        }
        return day;
    }

    private int getEstateIndex(){
        return getInt(1);
    }

    private int getInt(int index){
        try{
            return Integer.parseInt(parsedCommandLine.args.get(index));
        }catch(NumberFormatException e){
            System.out.println("You need to specify a valid integer at " + index);
            return -1;
        }
    }


}

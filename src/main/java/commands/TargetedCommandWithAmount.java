package commands;

import ethereum.AccountsManager;
import org.adridadou.ethereum.values.EthAccount;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class TargetedCommandWithAmount extends CLICommand {

    AccountsManager accountsManager;

    TargetedCommandWithAmount(AccountsManager accountsManager){
        this.accountsManager = accountsManager;
    }

    @Override
    public void execute() {
        String targetName;
        int amount;
        if(argcCorrect() && (targetName = getTargetName()) != null && (amount = getAmount()) != -1)
            onArgumentsCorrect(targetName, amount);
    }

    @Override
    public List<String> getRequiredArgumentsList() {
        return Arrays.asList("targetUserName", "amount");
    }

    @Override
    public List<String> getOptionalArgumentsList() {
        return new ArrayList<>();
    }

    abstract void onArgumentsCorrect(String targetName, int amount);

    private boolean argcCorrect(){
        if(parsedCommandLine.args.size() < 2){
            System.out.println("This command requires the userName of the target and the amount you wish to be minted");
            return false;
        }
        return true;
    }

    private String getTargetName(){
        String targetName = parsedCommandLine.args.get(0);
        EthAccount targetAccount = accountsManager.getAccount(targetName);
        if(targetAccount == null) {
            System.out.println("Error: target does not exist!");
            return null;
        }
        return targetName;
    }

    private int getAmount(){
        try{
            return Integer.parseInt(parsedCommandLine.args.get(1));
        }catch(NumberFormatException e){
            System.out.println("You need to specify a valid amount");
            return -1;
        }
    }

}

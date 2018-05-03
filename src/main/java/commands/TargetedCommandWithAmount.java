package commands;

import ethereum.AccountsManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class TargetedCommandWithAmount extends CLICommand {

    AccountsManager accountsManager;

    TargetedCommandWithAmount(AccountsManager accountsManager, String cmdName){
        super(cmdName);
        this.accountsManager = accountsManager;
    }

    @Override
    public void execute() {
        String targetName;
        int amount;
        if(argcCorrect(2) && (targetName = getName(accountsManager, 0)) != null && (amount = getInt(1)) != -1)
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


}

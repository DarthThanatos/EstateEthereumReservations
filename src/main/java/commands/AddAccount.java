package commands;

import ethereum.AccountsManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AddAccount extends CLICommand {

    private AccountsManager accountsManager;

    public AddAccount(AccountsManager accountsManager, String cmdName) {
        super(cmdName);
        this.accountsManager = accountsManager;
    }

    @Override
    public void execute() {
        if(argcCorrect(1)) {
            String name = parsedCommandLine.args.get(0);
            tryToAddAccount(name);
        }
    }


    private void tryToAddAccount(String name){
        try {
            accountsManager.addToAccounts(name);
            System.out.println("Warning: this is a brand new account with 0 ether balance. Inform the main account to sent this new user some resources.");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public List<String> getRequiredArgumentsList() {
        return Collections.singletonList("newusername");
    }

    @Override
    public List<String> getOptionalArgumentsList() {
        return new ArrayList<>();
    }

    @Override
    public String getDescription() {
        return "Adds new user with the specified name. Note: names have to be unique";
    }
}

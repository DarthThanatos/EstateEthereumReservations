package ethtest.commands;

import ethtest.AccountsManager;
import org.adridadou.ethereum.EthereumFacade;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AddAccount extends CLICommand {

    private AccountsManager accountsManager;

    public AddAccount(AccountsManager accountsManager){
        this.accountsManager = accountsManager;
    }

    @Override
    public void execute() {
        if(parsedCommandLine.args.size() == 0){
            System.out.println("This command takes a name of a new user as an argument. Note that this name must be unique.");
            return;
        }
        String name = parsedCommandLine.args.get(0);
        try {
            accountsManager.addToAccounts(name);
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

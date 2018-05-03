package commands;

import ethereum.AccountsManager;
import cli.UserCLI;
import org.adridadou.ethereum.EthereumFacade;
import org.adridadou.ethereum.values.EthAccount;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class UserConsoleCommand extends CLICommand {

    private UserCLI userCLI;
    private AccountsManager accountsManager;

    public UserConsoleCommand(EthereumFacade ethereum, AccountsManager accountsManager, String cmdName){
        super(cmdName);
        userCLI = new UserCLI(ethereum, accountsManager);
        this.accountsManager = accountsManager;
    }

    @Override
    public void execute() {
        String name;
        if(argcCorrect(1) && (name = getName(accountsManager,0))!= null) {
            userCLI.setUserName(name);
            userCLI.mainLoop();
        }
    }

    @Override
    public List<String> getRequiredArgumentsList() {
        return Collections.singletonList("name");
    }

    @Override
    public List<String> getOptionalArgumentsList() {
        return new ArrayList<>();
    }

    @Override
    public String getDescription() {
        return "Login as a user with the given name. This name has to exist.";
    }



}

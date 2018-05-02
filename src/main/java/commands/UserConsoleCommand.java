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

    public UserConsoleCommand(EthereumFacade ethereum, AccountsManager accountsManager){
        userCLI = new UserCLI(ethereum, accountsManager);
        this.accountsManager = accountsManager;
    }

    @Override
    public void execute() {
        String name;
        if(argcCorrect() && (name = getName())!= null) {
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


    private boolean argcCorrect(){
        if(parsedCommandLine.args.size() < 1){
            System.out.println("This command requires a user name as an argument. Type \'help\' for more information.");
            return false;
        }
        return true;
    }

    private String getName(){
        String name = parsedCommandLine.args.get(0);
        EthAccount account = accountsManager.getAccount(name);
        if(account == null){
            System.out.println("There is no user of such name, check if you spelled the name correctly, or add new account (see \'add\' command).");
            return null;
        }
        return name;
    }
}

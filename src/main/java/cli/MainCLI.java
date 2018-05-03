package cli;

import ethereum.AccountsManager;
import commands.*;
import org.adridadou.ethereum.EthereumFacade;

import java.util.HashMap;

public class MainCLI extends CLI {

    private HashMap<String, CLICommand> commands;

    public MainCLI(EthereumFacade ethereum, AccountsManager accountsManager) {
        super(ethereum, accountsManager);
    }

    @Override
    public HashMap<String, CLICommand> getCommands() {
        if(commands != null) return commands;

        commands = new HashMap<>();
        commands.put("exit", new ExitCommand(ethereum, "exit"));
        commands.put("logas", new UserConsoleCommand(ethereum, accountsManager, "logas"));
        commands.put("eb", new PrintEtherBalanceCommand(accountsManager,"eb"));
        commands.put("ccb", new PrintCustomCurrencyBalanceCommand(accountsManager, "ccb"));
        commands.put("users", new PrintUsersCommand(accountsManager, "users"));
        commands.put("add", new AddAccount(accountsManager,"add"));
        HelpCommand.addHelpToCommands(commands, this); //this needs to be called as the last one

        return commands;
    }

    @Override
    protected String getPrompt() {
        return "> ";
    }
}

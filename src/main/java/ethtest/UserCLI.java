package ethtest;

import ethtest.commands.*;
import org.adridadou.ethereum.EthereumFacade;

import java.util.HashMap;

public class UserCLI extends CLI {

    private String userName;
    private HashMap<String, CLICommand> commands;

    public UserCLI(EthereumFacade ethereum, AccountsManager accountsManager) {
        super(ethereum, accountsManager);
    }

    @Override
    public HashMap<String, CLICommand> getCommands() {
        if(commands != null) return commands;

        commands = new HashMap<>();
        commands.put("logout", new StopCommand(this));
        commands.put("mint", new MintCustomCurrencyCommand(accountsManager, this));
        commands.put("se", new SendEtherCommand(accountsManager, ethereum, this));
        commands.put("pc", new PayCustomCurrencyCommand(accountsManager, this));
        commands.put("eb", new PrintEtherBalanceCommand(accountsManager));
        commands.put("ccb", new PrintCustomCurrencyBalanceCommand(accountsManager));
        HelpCommand.addHelpToCommands(commands, this); //this needs to be called as the last one

        return commands;
    }

    @Override
    protected String getPrompt() {
        return userName + "> ";
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }
}

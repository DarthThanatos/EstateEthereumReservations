package cli;

import ethereum.AccountsManager;
import commands.*;
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
        commands.put("logout", new StopCommand(this, "logout"));
        commands.put("mint", new MintCustomCurrencyCommand(accountsManager, this, "mint"));
        commands.put("se", new SendEtherCommand(accountsManager, ethereum, this, "se"));
        commands.put("pc", new PayCustomCurrencyCommand(accountsManager, this, "pc"));
        commands.put("eb", new PrintEtherBalanceCommand(accountsManager, "eb"));
        commands.put("ccb", new PrintCustomCurrencyBalanceCommand(accountsManager, "ccb"));
        commands.put("pub", new PublishEstateCommand(accountsManager, this, "pub"));
        commands.put("estate", new PrintEstateCommand(accountsManager, this, "estate"));
        commands.put("ls", new ListAllEstatesCommand(accountsManager, this, "ls"));
        commands.put("mr", new MakeReservationCommand(accountsManager, this, "mr"));
        commands.put("cr", new CancelReservationCommand(accountsManager, this, "cr"));
        commands.put("ca", new ChangeAvailabilityStatusCommand(accountsManager, this, "ca"));
        commands.put("pfr", new PayForReservationCommand(accountsManager, this, "pfr"));
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

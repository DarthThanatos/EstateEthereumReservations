package commands;

import ethereum.AccountsManager;
import org.adridadou.ethereum.values.EthAccount;

import java.util.List;

public abstract class CLICommand {

    ParsedCommandLine parsedCommandLine;

    public abstract void execute();
    public abstract List<String> getRequiredArgumentsList();
    public abstract List<String> getOptionalArgumentsList();
    public abstract String getDescription();

    private String cmdName;

    public CLICommand(String cmdName){
        this.cmdName = cmdName;
    }

    public void setParsedCommandLine(ParsedCommandLine parsedCommandLine) {
        this.parsedCommandLine = parsedCommandLine;
    }

    boolean argcCorrect(int expectedAmount){
        if(parsedCommandLine.args.size() < expectedAmount){
            System.out.println("Usage: " + HelpCommand.calculateCommandHelpText(cmdName, this));
            return false;
        }
        return true;

    }


    @SuppressWarnings("SameParameterValue")
    String getName(AccountsManager accountsManager, int argIndex){
        String targetName = parsedCommandLine.args.get(argIndex);
        EthAccount targetAccount = accountsManager.getAccount(targetName);
        if(targetAccount == null) {
            System.out.println("Error: target does not exist!");
            return null;
        }
        return targetName;
    }

    @SuppressWarnings("SameParameterValue")
    EthAccount getAccount(AccountsManager accountsManager, int argIndex){
        String estateOwnerName = parsedCommandLine.args.get(argIndex);
        EthAccount account = accountsManager.getAccount(estateOwnerName);
        if(account == null){
            System.out.println("There is no user with such a name");
        }
        return  account;
    }

    int getInt(int argIndex){
        try{
            return Integer.parseInt(parsedCommandLine.args.get(argIndex));
        }catch(NumberFormatException e){
            System.out.println("You need to specify a valid integer at " + argIndex);
            return -1;
        }
    }

    int getDay(int argIndex){
        int day = getInt(argIndex);
        if(day < 0 || day > 6){
            System.out.println("Day needs to be in range [0,6]");
            return -1;
        }
        return day;
    }
}

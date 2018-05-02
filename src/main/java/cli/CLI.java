package cli;

import ethereum.AccountsManager;
import commands.CLICommand;
import commands.ParsedCommandLine;
import org.adridadou.ethereum.EthereumFacade;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;

public abstract class CLI {

    final AccountsManager accountsManager;
    final EthereumFacade ethereum;
    private Boolean shouldContinue = true;

    CLI(EthereumFacade ethereum, AccountsManager accountsManager){
        this.ethereum = ethereum;
        this.accountsManager = accountsManager;
    }

    private BufferedReader commandsReader = new BufferedReader(new InputStreamReader(System.in));

    public abstract HashMap<String, CLICommand> getCommands();
    protected abstract String getPrompt();

    public void mainLoop(){
        shouldContinue = true;
        while(shouldContinue){
            System.out.print(getPrompt());
            ParsedCommandLine parsedCommandLine = readAndParseLine();
            onParsedCommandLine(parsedCommandLine);
        }
    }

    private void onParsedCommandLine(ParsedCommandLine parsedCommandLine){
        if(parsedCommandLine == null)  return;
        if(parsedCommandLine.empty) return;
        HashMap<String, CLICommand> commands = getCommands();
        if(!commands.containsKey(parsedCommandLine.command)){
            if(!parsedCommandLine.command.isEmpty())
                System.out.println("Could not recognize command: \'" + parsedCommandLine.command + "\'. Type \'help\' to see available commands");
            return;
        }
        CLICommand cliCommand = commands.get(parsedCommandLine.command);
        cliCommand.setParsedCommandLine(parsedCommandLine);
        cliCommand.execute();
    }

    private ParsedCommandLine readAndParseLine(){
        try {
            String commandLine = commandsReader.readLine();
            return new ParsedCommandLine().parse(commandLine.trim());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void setShouldContinue(Boolean shouldContinue){
        this.shouldContinue = shouldContinue;
    }
}

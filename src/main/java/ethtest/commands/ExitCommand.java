package ethtest.commands;

import org.adridadou.ethereum.EthereumFacade;

import java.util.ArrayList;
import java.util.List;

public class ExitCommand extends CLICommand {

    private EthereumFacade ethereum;

    public ExitCommand(EthereumFacade ethereum){
        this.ethereum = ethereum;
    }

    @Override
    public void execute() {
        ethereum.shutdown();
        System.exit(0);
    }

    @Override
    public List<String> getRequiredArgumentsList() {
        return new ArrayList<>();
    }

    @Override
    public List<String> getOptionalArgumentsList() {
        return new ArrayList<>();
    }

    @Override
    public String getDescription() {
        return "Stops this program.";
    }
}

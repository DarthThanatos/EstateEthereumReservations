package ethtest.commands;

import ethtest.CLI;

import java.util.ArrayList;
import java.util.List;

public class StopCommand extends CLICommand {

    private CLI cli;

    public StopCommand(CLI cli){
        this.cli = cli;
    }

    @Override
    public void execute() {
        cli.setShouldContinue(false);
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
        return "Returns to the parent level of CLI hierarchy.";
    }
}

package ethtest.commands;

import java.util.List;

public abstract class CLICommand {

    ParsedCommandLine parsedCommandLine;

    public abstract void execute();
    public abstract List<String> getRequiredArgumentsList();
    public abstract List<String> getOptionalArgumentsList();
    public abstract String getDescription();

    public void setParsedCommandLine(ParsedCommandLine parsedCommandLine) {
        this.parsedCommandLine = parsedCommandLine;
    }
}

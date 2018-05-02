package commands;

import cli.CLI;
import java.util.*;

public class HelpCommand extends CLICommand {

    private String helpText;
    private CLI cli;

    private HelpCommand(CLI cli){
        this.cli = cli;
    }

    public static void addHelpToCommands(HashMap<String, CLICommand> commands, CLI cli){
        HelpCommand helpCommand = new HelpCommand(cli);
        commands.put("help", helpCommand);
        helpCommand.calculateHelpText(commands);
        // ^ we need to pass commands here, as it may not be complete yet, and to avoid infinite recursion, as cli.getCommands may call this function
    }

    private void calculateHelpText(HashMap<String, CLICommand> commands){
        helpText = "Usage: COMMAND [ARGS]\n\nAvailable commands: ";
        for(Map.Entry<String, CLICommand> commandEntry : commands.entrySet()){
            //noinspection StringConcatenationInLoop
            helpText += "\n\t" +  calculateCommandHelpText(commandEntry.getKey(), commandEntry.getValue());
        }
    }

    private String calculateCommandHelpText(String commandName, CLICommand cliCommand){
        List<String> requiredArgumentNamesList = cliCommand.getRequiredArgumentsList();
        List<String> optionalArgumentNamesList = cliCommand.getOptionalArgumentsList();

        @SuppressWarnings("ConstantConditions")
        String requiredArgumentNameString = requiredArgumentNamesList.isEmpty() ? "" : requiredArgumentNamesList.stream().reduce((agg, arg) -> agg + " " + arg).get();

        @SuppressWarnings("ConstantConditions")
        String optionalArgumentNameString = optionalArgumentNamesList.isEmpty() ? "" : optionalArgumentNamesList.stream().reduce( "" ,(agg, arg) -> agg + "[" + arg + "] ");

        return commandName + " " + requiredArgumentNameString + " " + optionalArgumentNameString + " - " + cliCommand.getDescription();
    }

    @Override
    public void execute() {
        if(parsedCommandLine.args.size() == 0)
            System.out.println(helpText);
        else{
            printCommandSpecifics(parsedCommandLine.args.get(0));
        }
    }

    private void printCommandSpecifics(String commandName){
        if(cli.getCommands().containsKey(commandName)){
            System.out.println(calculateCommandHelpText(commandName, cli.getCommands().get(commandName)));
        }
        else{
            System.out.println("There exists no such command: " + commandName);
        }
    }

    @Override
    public List<String> getRequiredArgumentsList() {
        return new ArrayList<>();
    }

    @Override
    public List<String> getOptionalArgumentsList() {
        return Collections.singletonList("commandName");
    }

    @Override
    public String getDescription() {
        return "Prints this help";
    }
}
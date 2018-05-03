package commands;

import ethereum.AccountsManager;

import java.util.ArrayList;
import java.util.List;

public abstract class PrintCommand extends CLICommand {

    private AccountsManager accountsManager;

    interface PrinterOfAll {
        void printAll(AccountsManager accountsManager);
    }

    interface PrinterOfOne{
        void printOne(AccountsManager accountsManager, String name);
    }

    PrinterOfAll printerOfAll;
    PrinterOfOne printerOfOne;

    PrintCommand(AccountsManager accountsManager, String cmdName){
        super(cmdName);
        this.accountsManager = accountsManager;
    }

    @Override
    public void execute() {
        if(parsedCommandLine.args.size() == 0) printerOfAll.printAll(accountsManager);
        else printerOfOne.printOne(accountsManager, parsedCommandLine.args.get(0));
    }

    @Override
    public List<String> getRequiredArgumentsList() {
        return new ArrayList<>();
    }

}

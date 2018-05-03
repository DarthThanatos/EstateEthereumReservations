package commands;

import ethereum.AccountsManager;
import java.util.Collections;
import java.util.List;

public class PrintUsersCommand extends PrintCommand {

    public PrintUsersCommand(AccountsManager accountsManager, String cmdName) {
        super(accountsManager, cmdName);
        printerOfAll = AccountsManager::printAccounts;
        printerOfOne = AccountsManager::printAccount;
    }

    @Override
    public List<String> getOptionalArgumentsList() {
        return Collections.singletonList("userName");
    }

    @Override
    public String getDescription() {
        return "Prints all users if no username is specified. Prints information about a specific user given their name.";
    }
}

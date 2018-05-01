package ethtest.commands;

import ethtest.AccountsManager;
import java.util.Collections;
import java.util.List;

public class PrintUsersCommand extends PrintCommand {

    public PrintUsersCommand(AccountsManager accountsManager) {
        super(accountsManager);
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

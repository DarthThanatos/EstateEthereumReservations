package commands;

import ethereum.AccountsManager;

import java.util.Collections;
import java.util.List;

public class PrintEtherBalanceCommand extends PrintCommand {

    public PrintEtherBalanceCommand(AccountsManager accountsManager) {
        super(accountsManager);
        printerOfAll = AccountsManager::printEthereumBalances;
        printerOfOne = AccountsManager::printUserEthereumBalance;
    }

    @Override
    public List<String> getOptionalArgumentsList() {
        return Collections.singletonList("userName");
    }

    @Override
    public String getDescription() {
        return "Prints all user ether balances if no username is specified. Prints ether balance information about a specific user given their name.";
    }
}

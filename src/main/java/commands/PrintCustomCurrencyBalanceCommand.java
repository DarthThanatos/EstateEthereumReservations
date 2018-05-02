package commands;

import ethereum.AccountsManager;

import java.util.Collections;
import java.util.List;

public class PrintCustomCurrencyBalanceCommand extends PrintCommand {

    public PrintCustomCurrencyBalanceCommand(AccountsManager accountsManager) {
        super(accountsManager);
        printerOfAll = AccountsManager::printCustomCurrencyBalances;
        printerOfOne = AccountsManager::printUserCustomCurrencyBalance;
    }

    @Override
    public List<String> getOptionalArgumentsList() {
        return Collections.singletonList("userName");
    }

    @Override
    public String getDescription() {
        return "Prints all user custom currency balances if no username is specified. Prints custom currency balance information about a specific user given their name.";
    }
}

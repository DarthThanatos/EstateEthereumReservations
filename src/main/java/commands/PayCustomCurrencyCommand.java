package commands;

import ethereum.AccountsManager;
import interfaces.Coin;
import cli.UserCLI;

import java.util.concurrent.ExecutionException;

public class PayCustomCurrencyCommand extends TargetedCommandWithAmount {

    private UserCLI userCLI;

    public PayCustomCurrencyCommand(AccountsManager accountsManager, UserCLI userCLI) {
        super(accountsManager);
        this.userCLI = userCLI;
    }

    @Override
    void onArgumentsCorrect(String targetName, int amount) {
        Coin coin = accountsManager.getCoinForName(userCLI.getUserName());
        try {
            coin.send(accountsManager.getAccount(targetName), amount).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getDescription() {
        return "Pays specified amount of the custom currency, targeting the account of a user with the given name.";
    }
}

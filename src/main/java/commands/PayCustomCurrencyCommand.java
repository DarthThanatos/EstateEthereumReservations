package commands;

import ethereum.AccountsManager;
import interfaces.Coin;
import cli.UserCLI;

import java.util.concurrent.ExecutionException;

public class PayCustomCurrencyCommand extends TargetedCommandWithAmount {

    private UserCLI userCLI;

    public PayCustomCurrencyCommand(AccountsManager accountsManager, UserCLI userCLI, String cmdName) {
        super(accountsManager, cmdName);
        this.userCLI = userCLI;
    }

    @Override
    void onArgumentsCorrect(String targetName, int amount) {
        Coin coin = accountsManager.getCoinForName(userCLI.getUserName());
        sendAmount(coin, targetName, amount);
    }

    @Override
    public String getDescription() {
        return "Pays specified amount of the custom currency, targeting the account of a user with the given name.";
    }

    private void sendAmount(Coin coin, String targetName, int amount){
        try {
            coin.send(accountsManager.getAccount(targetName), amount).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

    }
}

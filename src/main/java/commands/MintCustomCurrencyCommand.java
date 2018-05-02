package commands;

import ethereum.AccountsManager;
import interfaces.Coin;
import cli.UserCLI;
import java.util.concurrent.ExecutionException;

public class MintCustomCurrencyCommand extends TargetedCommandWithAmount {

    private UserCLI userCLI;

    public MintCustomCurrencyCommand(AccountsManager accountsManager, UserCLI userCLI){
        super(accountsManager);
        this.userCLI = userCLI;
    }

    @Override
    public void execute() {
        warnNonMainUser();
        super.execute();

    }

    @Override
    void onArgumentsCorrect(String targetName, int amount) {
        Coin coin = accountsManager.getCoinForName(userCLI.getUserName());
        mintCoins(coin, targetName, amount);
    }

    private void warnNonMainUser(){
        if(!userCLI.getUserName().equals("main")){
            System.out.println("Warning to " + userCLI.getUserName() + ": you are not the owner of the contract. Your request will be ignored by the contract code.");
        }
    }

    private void mintCoins(Coin coin, String targetName, int amount){
        try {
            coin.mint(accountsManager.getAccount(targetName), amount).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getDescription() {
        return "Mints custom crypto currency, magically assigning a user with the specified name the specified amount";
    }
}
package ethtest.commands;

import ethtest.AccountsManager;
import ethtest.Coin;
import ethtest.UserCLI;
import java.util.concurrent.ExecutionException;

public class MintCustomCurrencyCommand extends TargetedCommandWithAmount {

    private UserCLI userCLI;

    public MintCustomCurrencyCommand(AccountsManager accountsManager, UserCLI userCLI){
        super(accountsManager);
        this.userCLI = userCLI;
    }

    @Override
    public void execute() {
        if(!userCLI.getUserName().equals("main")){
            System.out.println("Warning to " + userCLI.getUserName() + ": you are not the owner of the contract. Your request will be ignored by the contract code.");
        }
        super.execute();

    }

    @Override
    void onArgumentsCorrect(String targetName, int amount) {
        Coin coin = accountsManager.getCoinForName(userCLI.getUserName());
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
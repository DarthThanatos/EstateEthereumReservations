package commands;

import ethereum.AccountsManager;
import cli.UserCLI;
import org.adridadou.ethereum.EthereumFacade;
import org.adridadou.ethereum.values.EthAccount;

import java.util.concurrent.ExecutionException;

import static org.adridadou.ethereum.values.EthValue.ether;

public class SendEtherCommand extends TargetedCommandWithAmount {

    private EthereumFacade ethereum;
    private UserCLI userCLI;

    public SendEtherCommand(AccountsManager accountsManager, EthereumFacade ethereum, UserCLI userCLI, String cmdName) {
        super(accountsManager, cmdName);
        this.ethereum = ethereum;
        this.userCLI = userCLI;
    }

    @Override
    void onArgumentsCorrect(String targetName, int amount) {
        EthAccount srcAccount = accountsManager.getAccount(userCLI.getUserName());
        EthAccount targetAccount = accountsManager.getAccount(targetName);
        sendEtherFromTo(srcAccount, targetAccount, amount);
    }

    @Override
    public String getDescription() {
        return "Sends specified amount of ether to the account of a user with the given name.";
    }

    private void sendEtherFromTo(EthAccount srcAccount, EthAccount targetAccount, int amount){
        try {
            ethereum.sendEther(srcAccount, targetAccount.getAddress(), ether(getCorrectAmountToSend(amount))).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    private int getCorrectAmountToSend(int amount){
        int MAX_ETHER_AMOUNT = 10;
        if(amount > MAX_ETHER_AMOUNT) {
            System.out.println("Warning: sending only " + MAX_ETHER_AMOUNT + " as this is the maximum allowed amount to send");
            amount = MAX_ETHER_AMOUNT;
        }
        return amount;
    }

}

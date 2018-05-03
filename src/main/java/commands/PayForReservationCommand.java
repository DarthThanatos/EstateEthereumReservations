package commands;

import cli.UserCLI;
import ethereum.AccountsManager;
import interfaces.Coin;
import org.adridadou.ethereum.values.EthAccount;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class PayForReservationCommand extends CLICommand {

    private AccountsManager accountsManager;
    private UserCLI userCLI;

    public PayForReservationCommand(AccountsManager accountsManager, UserCLI userCLI, String cmdName){
        super(cmdName);
        this.accountsManager = accountsManager;
        this.userCLI = userCLI;
    }

    @Override
    public void execute() {
        EthAccount account;
        int estateIndex, amount, day;
        if(argcCorrect(4) && (account = getAccount(accountsManager, 0)) != null && (estateIndex = getInt(1))!= -1 && (amount = getInt(2))!= -1 && (day = getDay(3))!= -1){
            payForReservation(account, estateIndex, amount, day);
        }
    }

    private void payForReservation(EthAccount account, int estateIndex, int amount, int day){
        Coin coin = accountsManager.getCoinForName(userCLI.getUserName());
        try {
            coin.payForReservation(account, estateIndex, amount, day, accountsManager.getReservationsAddr()).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

    }

    @Override
    public List<String> getRequiredArgumentsList() {
        return Arrays.asList("estateOwner", "estateLocalIndex", "amount", "day");
    }

    @Override
    public List<String> getOptionalArgumentsList() {
        return new ArrayList<>();
    }

    @Override
    public String getDescription() {
        return "Pays for a specific day of as specific estate a specific amount of cryptocurrency";
    }





}

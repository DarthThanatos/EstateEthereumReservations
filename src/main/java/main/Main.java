package main;

import cli.MainCLI;
import ethereum.AccountsManager;
import org.adridadou.ethereum.EthereumFacade;
import org.adridadou.ethereum.provider.EthereumFacadeProvider;
import org.adridadou.ethereum.provider.EthereumJConfigs;


public class Main {

    public static boolean DEVEL_PHASE = false;

    private static final AccountsManager accountsManager = new AccountsManager();
    private static final EthereumFacade ethereum = forTest();

    public static  void main(String[] args) throws Exception{
        new MainCLI(ethereum, accountsManager).mainLoop();
    }

    private static EthereumFacade forTest(){
        DEVEL_PHASE = true;
        accountsManager.createStartingAccounts();
        EthereumFacade ethereum = EthereumFacadeProvider.forTest(accountsManager.createTestAccountsConfig());
        accountsManager.setEthereum(ethereum);
        return ethereum;
    }

    private static EthereumFacade forNetwork(){
        DEVEL_PHASE = false;
        accountsManager.createStartingAccounts();
        EthereumFacade ethereum = EthereumFacadeProvider.forNetwork(EthereumJConfigs.etherCampTestnet()).create();
        accountsManager.setEthereum(ethereum);
        return ethereum;
    }
}

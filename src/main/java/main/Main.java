package main;

import cli.MainCLI;
import ethereum.AccountsManager;
import org.adridadou.ethereum.EthereumFacade;
import org.adridadou.ethereum.blockchain.BlockchainConfig;
import org.adridadou.ethereum.provider.EthereumFacadeProvider;
import org.adridadou.ethereum.provider.EthereumJConfigs;
import org.adridadou.ethereum.values.config.GenesisPath;


public class Main{

    public static boolean DEVEL_PHASE = false;
    private static final String CONFIG_FILE = "custom_genesis.json";

    private static final AccountsManager accountsManager = new AccountsManager();
    private static final EthereumFacade ethereum = forNetwork();

    public static  void main(String[] args) throws Exception{
        new MainCLI(ethereum, accountsManager).mainLoop();
    }

    private static EthereumFacade forTest(){
        DEVEL_PHASE = true;
        accountsManager.createStartingAccounts(CONFIG_FILE);
        EthereumFacade ethereum = EthereumFacadeProvider.forTest(accountsManager.createTestAccountsConfig());
        accountsManager.setEthereum(ethereum);
        return ethereum;
    }


    private static EthereumFacade forNetwork(){
        System.err.println("Before for network");
        DEVEL_PHASE = false;
        accountsManager.createStartingAccounts(CONFIG_FILE);
        EthereumFacade ethereum =
                EthereumFacadeProvider.forNetwork( networkCOnfig() ).create();
        accountsManager.setEthereum(ethereum);
        System.err.println("After for network");
        return ethereum;
    }

    private static BlockchainConfig.Builder networkCOnfig() {
        return EthereumJConfigs.privateMiner().genesis(GenesisPath.path(CONFIG_FILE)).peerDiscovery(true);
    }

}

package main;

import cli.MainCLI;
import ethereum.AccountsManager;
import org.adridadou.ethereum.EthereumFacade;
import org.adridadou.ethereum.blockchain.BlockchainConfig;
import org.adridadou.ethereum.provider.EthereumFacadeProvider;
import org.adridadou.ethereum.provider.EthereumJConfigs;
import org.adridadou.ethereum.values.config.GenesisPath;
import org.adridadou.ethereum.values.config.NodeIp;
import org.adridadou.ethereum.values.config.ChainId;

import org.adridadou.ethereum.blockchain.IncompatibleDatabaseBehavior;

public class Main{

    public static boolean DEVEL_PHASE = false;
    private static ChainId CUSTOM_NETWORK_ID = ChainId.id(3765);
    private static final String CONFIG_FILE = "custom_genesis.json";

    private static final AccountsManager accountsManager = new AccountsManager();
    private static final EthereumFacade ethereum = forNetwork();

    public static  void main(String[] args) throws Exception{
        new MainCLI(ethereum, accountsManager).mainLoop();
    }

    private static EthereumFacade forTest(){
        DEVEL_PHASE = true;
        accountsManager.createStartingAccounts(CONFIG_FILE);
        accountsManager.printAccounts();
        EthereumFacade ethereum = EthereumFacadeProvider.forTest(accountsManager.createTestAccountsConfig());
        accountsManager.setEthereum(ethereum);
        return ethereum;
    }


    private static EthereumFacade forNetwork(){
        System.err.println("Before for network");
        DEVEL_PHASE = false;
        accountsManager.createStartingAccounts(CONFIG_FILE);
        accountsManager.printAccounts();
        EthereumFacade ethereum =
                EthereumFacadeProvider.forNetwork( privateNetwork() ).create();
        accountsManager.setEthereum(ethereum);
        System.err.println("After for network");
        return ethereum;
    }

    private static BlockchainConfig.Builder networkCOnfig() {
        return EthereumJConfigs.privateMiner().genesis(GenesisPath.path(CONFIG_FILE)).peerDiscovery(true);
    }
    
    private static BlockchainConfig.Builder privateNetwork() {
        return BlockchainConfig.builder()
                .addIp(NodeIp.ip("localhost:30302"))
                .addIp(NodeIp.ip("localhost:30303"))
                .networkId(CUSTOM_NETWORK_ID)
                .genesis(GenesisPath.path("genesis.json"))
                .incompatibleDatabaseBehavior(IncompatibleDatabaseBehavior.IGNORE);
    }
}

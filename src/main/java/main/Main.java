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
                .addIp(NodeIp.ip("192.168.0.100:30301"))
                .addIp(NodeIp.ip("192.168.0.100:30302"))
                .addIp(NodeIp.ip("192.168.0.100:30303"))
                .addIp(NodeIp.ip("192.168.0.101:30301"))
                .addIp(NodeIp.ip("192.168.0.101:30302"))
                .addIp(NodeIp.ip("192.168.0.101:30303"))
                .peerActiveUrl("enode://15e76f568c37cbc1d85eff477b15a8e8a9db728dc4f4bcdb44aabcdd4912903203978bb6ddeb76095fb34080bfa50f0bc4c251967f030f56adc72e891bb9cecf@192.168.0.100:30303")
                .peerActiveUrl("enode://85ed62ffce2ba244af6b499b6494cef00db661121b0f7a040f684ba2c5bd9441157e11e81087e1ba6213e15b533d891179f356822b2622a751d4701942eeb658@192.168.0.101:30302")
                .networkId(CUSTOM_NETWORK_ID)
                .genesis(GenesisPath.path("genesis.json"))
                .incompatibleDatabaseBehavior(IncompatibleDatabaseBehavior.IGNORE)
                .peerDiscovery(false);
    }
}

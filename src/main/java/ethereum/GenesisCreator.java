package ethereum;

import org.adridadou.ethereum.values.EthAccount;

import java.util.HashMap;

public class GenesisCreator {

    private HashMap<String, EthAccount>accountHashMap;
    private String outFileName;

    public GenesisCreator(HashMap<String, EthAccount>accountHashMap, String outFileName){
        this.accountHashMap = accountHashMap;
        this.outFileName = outFileName;
    }

    public void createJSONGenesis(){

    }
}

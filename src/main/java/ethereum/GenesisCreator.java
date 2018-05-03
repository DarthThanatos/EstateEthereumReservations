package ethereum;

import org.adridadou.ethereum.values.EthAccount;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

/*
{
        "alloc": {
        "defe833b759012a672234fe95b21e7fc671943a0": {
        "balance": "1606938044258990275541962092341162602522202993782792835301376"
        },
        "4583Bdbc22F83Bb2F9E5dC52F91785Eb2FD56097": {
        "balance": "1606938044258990275541962092341162602522202993782792835301376"
        }
        },

        "nonce": "0x0000000000000000",
        "difficulty": "0x01",
        "mixhash": "0x0000000000000000000000000000000000000000000000000000000000000000",
        "coinbase": "0x0000000000000000000000000000000000000000",
        "timestamp": "0x00",
        "parentHash": "0x0000000000000000000000000000000000000000000000000000000000000000",
        "extraData": "0x11bbe8db4e347b4e8c937c1c8370e4b5ed33adb3db69cbdb7a38e1e50b1b82fa",
        "gasLimit": "0x5b8d80"
        }
*/

 class GenesisCreator {

    private HashMap<String, EthAccount>accountHashMap;
    private String outFileName;

    GenesisCreator(HashMap<String, EthAccount>accountHashMap, String outFileName){
        this.accountHashMap = accountHashMap;
        this.outFileName = outFileName;
    }

     void createJSONGenesis(){
         FileWriter genesisFileWriter;
         if((genesisFileWriter = getFileWriter()) != null) {
             GenesisJSON genesisJSON = new GenesisJSON().alloc().nonce().difficulty().mixHash().coinbase().timestamp().parentHash().extraData().gasLimit();
             writeGenesisAndClose(genesisFileWriter, genesisJSON);
             System.out.println("Created genesis file: " + outFileName);
         }
     }

     private void writeGenesisAndClose(FileWriter genesisFileWriter, GenesisJSON genesisJSON){
         try {
             genesisFileWriter.write(genesisJSON.toString(4));
         } catch (IOException e) {
             e.printStackTrace();
         }
         finally {
             try {
                 genesisFileWriter.flush();
                 genesisFileWriter.close();
             } catch (IOException e) {
                 e.printStackTrace();
             }
         }
     }

     private FileWriter getFileWriter(){
         FileWriter genesisFileWriter;
         try {
             genesisFileWriter = new FileWriter(getGenesisFile());
         } catch (IOException e) {
             e.printStackTrace();
             return null;
         }
         return genesisFileWriter;
     }

     private File getGenesisFile(){
         File file = new File("src/main/resources/genesis/" + outFileName);
         boolean created = false;
         try {
             created = file.createNewFile();
         } catch (IOException e) {
             e.printStackTrace();
         }
         if(!created) System.out.println("File " + outFileName + " already exists, not creating");
         return  file;
     }

    private class GenesisJSON extends JSONObject{

        GenesisJSON alloc(){
            JSONObject alloc = new JSONObject();
            accountHashMap.values().forEach(
                    account -> alloc.put(
                            account.getAddress().toString(),
                            new JSONObject().put("balance","1606938044258990275541962092341162602522202993782792835301376")
                    )
            );
            put("alloc", alloc);
            return this;
        }

        GenesisJSON nonce(){
            put("nonce", "0x0000000000000000");
            return this;
        }

        GenesisJSON difficulty(){
            put("difficulty", "0x01");
            return this;
        }

        GenesisJSON mixHash(){
            put("mixhash", "0x0000000000000000000000000000000000000000000000000000000000000000");
            return this;
        }

        GenesisJSON coinbase(){
            put("coinbase", "0x0000000000000000000000000000000000000000");
            return this;
        }

        GenesisJSON timestamp(){
            put("timestamp", "0x00");
            return this;
        }

        GenesisJSON parentHash(){
            put("parentHash", "0x0000000000000000000000000000000000000000000000000000000000000000");
            return this;
        }

        GenesisJSON extraData(){
            put("extraData", "0x11bbe8db4e347b4e8c937c1c8370e4b5ed33adb3db69cbdb7a38e1e50b1b82fa");
            return this;
        }

        GenesisJSON gasLimit(){
            put("gasLimit", "0x5b8d80");
            return this;
        }
    }


}

package ethereum;

import org.adridadou.ethereum.EthereumFacade;
import org.adridadou.ethereum.values.CompiledContract;
import org.adridadou.ethereum.values.EthAccount;
import org.adridadou.ethereum.values.EthAddress;
import org.adridadou.ethereum.values.SoliditySource;

import java.io.File;

class ContractPublisher {

    private EthereumFacade ethereum;

    ContractPublisher(EthereumFacade ethereum){
        this.ethereum = ethereum;
    }

    <T> Contract<T> compileAndPublish(String contractFileName, String contractName, EthAccount publisherAccount, Class<T> interfaceClass)throws Exception{
        Contract<T> contract = new Contract<>();
        contract.contractSrc = SoliditySource.from(new File("contracts/" + contractFileName));
        contract.compiledContract = ethereum.compile(contract.contractSrc, contractName).get();
        contract.contractAddress = ethereum.publishContract(contract.compiledContract, publisherAccount).get();
        contract.proxy =  ethereum.createContractProxy(contract.compiledContract, contract.contractAddress, publisherAccount,interfaceClass);
        return contract;
    }

    static class Contract <T>{
        SoliditySource contractSrc;
        CompiledContract compiledContract;
        EthAddress contractAddress;
        T proxy;
    }
}

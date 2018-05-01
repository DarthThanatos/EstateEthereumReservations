package ethtest;

import org.adridadou.ethereum.values.EthAddress;

import java.math.BigInteger;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface MyContractInterface {

    CompletableFuture<Integer> myMethod(String value);
    CompletableFuture<Void> myMethod2(String value);
    String getI1();
    String getI2();
    Boolean getT();
    EthAddress getOwner();
    List<BigInteger> getArray();

}
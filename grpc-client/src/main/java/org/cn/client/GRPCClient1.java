package org.cn.client;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.cn.stubs.Bank;
import org.cn.stubs.BankServiceGrpc;

public class GRPCClient1 {
    public static void main(String[] args) {
        ManagedChannel managedChannel = ManagedChannelBuilder.forAddress("localhost", 1111)
                .usePlaintext()
                .build();
        BankServiceGrpc.BankServiceBlockingStub stub = BankServiceGrpc.newBlockingStub(managedChannel);
        Bank.ConvertCurrencyRequest request = Bank.ConvertCurrencyRequest.newBuilder()
                .setCurrencyFrom("MAD")
                .setCurrencyTo("USD")
                .setAmount(4000)
                .build();
        Bank.ConvertCurrencyResponse response = stub.convert(request);
        System.out.println(response);
    }
}

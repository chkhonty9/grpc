package org.cn.client;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import org.cn.stubs.Bank;
import org.cn.stubs.BankServiceGrpc;

import java.io.IOException;

public class GRPCClient2 {
    public static void main(String[] args) throws IOException {
        ManagedChannel managedChannel = ManagedChannelBuilder.forAddress("localhost", 1111)
                .usePlaintext()
                .build();
        BankServiceGrpc.BankServiceStub asyncStub = BankServiceGrpc.newStub(managedChannel);
        Bank.ConvertCurrencyRequest request = Bank.ConvertCurrencyRequest.newBuilder()
                .setCurrencyFrom("MAD")
                .setCurrencyTo("USD")
                .setAmount(4000)
                .build();
        asyncStub.convert(request, new StreamObserver<Bank.ConvertCurrencyResponse>() {

            @Override
            public void onNext(Bank.ConvertCurrencyResponse convertCurrencyResponse) {
                System.out.println("----------------------------------");
                System.out.println(convertCurrencyResponse);
                System.out.println("----------------------------------");
            }

            @Override
            public void onError(Throwable throwable) {
                System.out.println("Error: " + throwable.getMessage());
            }

            @Override
            public void onCompleted() {
                System.out.println(" the end ^_^!");
            }
        });
        System.out.println("hello");
        System.in.read();
    }
}

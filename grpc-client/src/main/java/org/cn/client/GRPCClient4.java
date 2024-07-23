package org.cn.client;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import org.cn.stubs.Bank;
import org.cn.stubs.BankServiceGrpc;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class GRPCClient4 {
    public static void main(String[] args) throws IOException {
        ManagedChannel managedChannel = ManagedChannelBuilder.forAddress("localhost", 1111)
                .usePlaintext()
                .build();
        BankServiceGrpc.BankServiceStub asyncStub = BankServiceGrpc.newStub(managedChannel);

        StreamObserver<Bank.ConvertCurrencyRequest> performStream = asyncStub.performStream(new StreamObserver<Bank.ConvertCurrencyResponse>() {
            @Override
            public void onNext(Bank.ConvertCurrencyResponse convertCurrencyResponse) {
                System.out.println("-------------------------------");
                System.out.println(convertCurrencyResponse);
                System.out.println("-------------------------------");
            }

            @Override
            public void onError(Throwable throwable) {
                System.out.println("Error: " + throwable.getMessage());
            }

            @Override
            public void onCompleted() {
                System.out.println("Completed ^_^!");
            }
        });
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            int counter = 0;
            @Override
            public void run() {
                Bank.ConvertCurrencyRequest request = Bank.ConvertCurrencyRequest.newBuilder()
                        .setAmount(Math.random() * 100)
                        .build();
                performStream.onNext(request);
                System.out.println("-- counter: " + counter);
                System.out.println("-- amount: " + Math.random() * 100);
                ++counter;
                if (counter == 10) {
                    performStream.onCompleted();
                    timer.cancel();
                }
            }
        }, 1000, 1000);
        System.out.println("hello");
        System.in.read();
    }
}

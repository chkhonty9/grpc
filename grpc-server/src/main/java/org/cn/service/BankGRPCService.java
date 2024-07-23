package org.cn.service;

import io.grpc.stub.StreamObserver;
import org.cn.stubs.Bank;
import org.cn.stubs.BankServiceGrpc;

import java.util.Timer;
import java.util.TimerTask;

public class BankGRPCService extends BankServiceGrpc.BankServiceImplBase {

    @Override
    public void convert(Bank.ConvertCurrencyRequest request, StreamObserver<Bank.ConvertCurrencyResponse> responseObserver) {
        String currencyFrom = request.getCurrencyFrom();
        String currencyTo = request.getCurrencyTo();
        double amount = request.getAmount();
        Bank.ConvertCurrencyResponse response = Bank.ConvertCurrencyResponse.newBuilder()
                .setCurrencyFrom(currencyFrom)
                .setCurrencyTo(currencyTo)
                .setAmount(amount)
                .setResult(amount * 9.80)
                .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void getCurrencyStream(Bank.ConvertCurrencyRequest request, StreamObserver<Bank.ConvertCurrencyResponse> responseObserver) {
        String currencyFrom = request.getCurrencyFrom();
        String currencyTo = request.getCurrencyTo();
        double amount = request.getAmount();
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            int counter = 0;
            @Override
            public void run() {
                Bank.ConvertCurrencyResponse response = Bank.ConvertCurrencyResponse.newBuilder()
                        .setCurrencyFrom(currencyFrom)
                        .setCurrencyTo(currencyTo)
                        .setAmount(amount)
                        .setResult(amount * Math.random() * 100)
                        .build();
                responseObserver.onNext(response);
                ++counter;
                if (counter == 10) {
                    responseObserver.onCompleted();
                    timer.cancel();
                }
            }
        }, 1000, 1000);
    }

    @Override
    public StreamObserver<Bank.ConvertCurrencyRequest> performStream(StreamObserver<Bank.ConvertCurrencyResponse> responseObserver) {
       return new StreamObserver<Bank.ConvertCurrencyRequest>() {
           double sum = 0.0;
           @Override
           public void onNext(Bank.ConvertCurrencyRequest convertCurrencyRequest) {
               sum += convertCurrencyRequest.getAmount();
           }

           @Override
           public void onError(Throwable throwable) {
               System.out.println(throwable.getMessage());
           }

           @Override
           public void onCompleted() {
               Bank.ConvertCurrencyResponse response = Bank.ConvertCurrencyResponse.newBuilder()
                       .setResult(sum* 100)
                       .build();
               responseObserver.onNext(response);
               responseObserver.onCompleted();
           }
       };
    }

    @Override
    public StreamObserver<Bank.ConvertCurrencyRequest> fullStream(StreamObserver<Bank.ConvertCurrencyResponse> responseObserver) {
        return new StreamObserver<Bank.ConvertCurrencyRequest>() {
            @Override
            public void onNext(Bank.ConvertCurrencyRequest convertCurrencyRequest) {
                Bank.ConvertCurrencyResponse response = Bank.ConvertCurrencyResponse.newBuilder()
                        .setResult(convertCurrencyRequest.getAmount() * Math.random() * 10)
                        .build();
                responseObserver.onNext(response);
            }

            @Override
            public void onError(Throwable throwable) {
                System.out.println(throwable.getMessage());
            }

            @Override
            public void onCompleted() {
                responseObserver.onCompleted();
            }
        };
    }
}

package org.cn.server;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import org.cn.service.BankGRPCService;


public class GRPCServer {
    public static void main(String[] args) throws Exception {
        Server server = ServerBuilder.forPort(1111)
                .addService(new BankGRPCService())
                .build();
        server.start();
        server.awaitTermination();
    }
}

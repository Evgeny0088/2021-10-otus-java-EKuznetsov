package ru.otus.protobuf.server;

import io.grpc.ServerBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.protobuf.serverData.ServerMetaData;

import java.io.IOException;

public class GRPCServer {

    private static final Logger log = LoggerFactory.getLogger(GRPCServer.class);

    public static void main(String[] args) throws IOException, InterruptedException {
        var numberOrderService = new NumberOrderServiceImpl();
        var server = ServerBuilder
                .forPort(ServerMetaData.SERVER_PORT)
                .addService(numberOrderService).build();
        server.start();

        Runtime.getRuntime().addShutdownHook(new Thread( () -> {
            server.shutdown();
            log.info("server is being stopped...");
        }));

        log.info("server waiting for client connections...");
        server.awaitTermination();
    }
}

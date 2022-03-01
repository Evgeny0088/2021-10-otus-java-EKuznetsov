package ru.otus.protobuf.client;

import io.grpc.ManagedChannelBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.protobuf.generated.NumberOrderServiceGrpc;
import ru.otus.protobuf.generated.SequenceOrderRequest;
import ru.otus.protobuf.serverData.ServerMetaData;

public class GRPCClient {

    private static final Logger log = LoggerFactory.getLogger(GRPCClient.class);

    public static void main(String[] args){
        var channel = ManagedChannelBuilder
                .forAddress(ServerMetaData.SERVER_HOST, ServerMetaData.SERVER_PORT)
                .usePlaintext()
                .build();
        var clientStub = NumberOrderServiceGrpc.newStub(channel);
        ClientResponseObserver observer = new ClientResponseObserver();
        clientStub.getSequence(createRequestForSequence(),observer);
        ClientSequenceProcessor processor = new ClientSequenceProcessor(observer);
        processor.process();
        log.warn("client server is shutting down!...");
        channel.shutdown();
    }

    private static SequenceOrderRequest createRequestForSequence(){
        return SequenceOrderRequest.newBuilder().setFirst(0).setLast(30).build();
    }

}

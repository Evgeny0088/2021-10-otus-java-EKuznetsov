package ru.otus.protobuf.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.protobuf.generated.SequenceGenerationResponse;

import java.util.concurrent.atomic.AtomicInteger;

public class ClientResponseObserver implements io.grpc.stub.StreamObserver<SequenceGenerationResponse>{

    private static final Logger log = LoggerFactory.getLogger(ClientResponseObserver.class);
    private final AtomicInteger inputFromServer = new AtomicInteger(0);
    public ClientResponseObserver() {
    }

    @Override
    public void onNext(SequenceGenerationResponse value) {
        inputFromServer.set(value.getBegin());
    }

    @Override
    public void onError(Throwable t) {
        log.error("error during the process is happening!...");
    }

    @Override
    public void onCompleted() {
        log.warn("server finished number generation, shutting down...");
    }

    public AtomicInteger getInputFromServer(){
        return inputFromServer;
    }
}
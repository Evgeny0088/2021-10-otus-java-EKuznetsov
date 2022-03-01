package ru.otus.protobuf.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.protobuf.generated.SequenceGenerationResponse;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class ClientResponseObserver implements io.grpc.stub.StreamObserver<SequenceGenerationResponse>{

    private static final Logger log = LoggerFactory.getLogger(ClientResponseObserver.class);

    private final BlockingQueue<SequenceGenerationResponse> generatedQueueFromServer;
    private final static int QUEUE_CAPACITY = 5;

    public ClientResponseObserver() {
        this.generatedQueueFromServer = new ArrayBlockingQueue<>(QUEUE_CAPACITY);
    }

    @Override
    public void onNext(SequenceGenerationResponse value) {
        boolean queueIsFull = generatedQueueFromServer.offer(value);
        if (!queueIsFull){
            log.warn("queue is full, wait a moment...");
        }
    }

    @Override
    public void onError(Throwable t) {
        log.error("error during the process is happening!...");
    }

    @Override
    public void onCompleted() {
        log.warn("server finished number generation, shutting down...");
    }

    public BlockingQueue<SequenceGenerationResponse> getGeneratedQueueFromServer(){
        return generatedQueueFromServer;
    }
}
package ru.otus.protobuf.server;

import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.protobuf.generated.*;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class NumberOrderServiceImpl extends NumberOrderServiceGrpc.NumberOrderServiceImplBase {

    private static final Logger log = LoggerFactory.getLogger(NumberOrderServiceImpl.class);

    private final ScheduledExecutorService executor = Executors.newScheduledThreadPool(3);
    private static final long ITERATION_INTERVAL = 2L;

    @Override
    public void getSequence(SequenceOrderRequest request, StreamObserver<SequenceGenerationResponse> responseObserver) {
        var startValue = new AtomicInteger(request.getFirst());
        Runnable generateNumbers = ()->{
            int nextValue = startValue.getAndIncrement();
            var sequenceGenerationResponse = SequenceGenerationResponse
                    .newBuilder()
                    .setBegin(nextValue).build();
            responseObserver.onNext(sequenceGenerationResponse);
            log.info("server current generated value >>> {}",nextValue);
            if (nextValue >= request.getLast()-1){
                log.warn("server generation is finished...");
                responseObserver.onCompleted();
                executor.shutdown();
            }
        };
        executor.scheduleAtFixedRate(generateNumbers, 0, ITERATION_INTERVAL, TimeUnit.SECONDS);
    }
}
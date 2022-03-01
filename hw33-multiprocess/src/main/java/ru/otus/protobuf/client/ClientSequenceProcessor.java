package ru.otus.protobuf.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.protobuf.generated.SequenceGenerationResponse;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class ClientSequenceProcessor {

    private static final Logger log = LoggerFactory.getLogger(ClientSequenceProcessor.class);

    private final ClientResponseObserver clientResponseObserver;
    private final BlockingQueue<SequenceGenerationResponse> generatedQueueFromServer;
    private ClientSequenceNumberHandler<BlockingQueue<SequenceGenerationResponse>> clientSequenceNumberHandler;
    private final AtomicInteger clientValue = new AtomicInteger(0);
    private final AtomicInteger iterationCounter = new AtomicInteger(0);
    private final ScheduledExecutorService queueingThread = Executors.newScheduledThreadPool(1);
    private static final int ITERATION_LIMIT = 50;
    private static final long ITERATION_INTERVAL = 1L;

    public ClientSequenceProcessor(ClientResponseObserver clientResponseObserver) {
        this.clientResponseObserver = clientResponseObserver;
        this.generatedQueueFromServer = this.clientResponseObserver.getGeneratedQueueFromServer();
    }

    public void process(){
        clientSequenceNumberHandler = (queue)->{
            try{
                if (iterationCounter.getAndIncrement()>=ITERATION_LIMIT-1){
                    log.warn("client process is shutting down >>> last iteration: {}",iterationCounter.get()-1);
                    queue.clear();
                    queueingThread.shutdown();
                }
                SequenceGenerationResponse serverResponse = queue.poll(ITERATION_INTERVAL,TimeUnit.SECONDS);
                int serverValue = serverResponse != null ? serverResponse.getBegin() : -1;
                log.info("server current value: {}",serverValue != -1 ? serverValue : "<not available!...>");
                log.info("client current value: {}",clientValue.getAndIncrement());
                clientValue.addAndGet(serverValue != -1 ? serverValue : 0);
                log.info("ITERATION RESULT >>> {}",clientValue.get());
            }catch (Exception e){
                log.error("error during client process!...", e);
                queue.clear();
                queueingThread.shutdown();
            }
        };
        queueingThread.scheduleAtFixedRate(()-> clientSequenceNumberHandler.accept(generatedQueueFromServer),0,ITERATION_INTERVAL, TimeUnit.SECONDS);
    }
}

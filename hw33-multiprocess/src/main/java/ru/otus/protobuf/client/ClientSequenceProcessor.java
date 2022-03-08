package ru.otus.protobuf.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class ClientSequenceProcessor {

    private static final Logger log = LoggerFactory.getLogger(ClientSequenceProcessor.class);

    private final ClientResponseObserver clientResponseObserver;
    private final AtomicInteger generatedServerNumber;
    private ClientSequenceNumberHandler<AtomicInteger> clientSequenceNumberHandler;
    private final AtomicInteger acceptedValueFromServer = new AtomicInteger(0);
    private final AtomicInteger clientValue = new AtomicInteger(0);
    private final AtomicInteger iterationCounter = new AtomicInteger(0);
    private final ScheduledExecutorService queueingThread = Executors.newScheduledThreadPool(1);
    private static final int ITERATION_LIMIT = 50;
    private static final long ITERATION_INTERVAL = 1L;

    public ClientSequenceProcessor(ClientResponseObserver clientResponseObserver) {
        this.clientResponseObserver = clientResponseObserver;
        this.generatedServerNumber = this.clientResponseObserver.getInputFromServer();
    }

    public void process(){
        clientSequenceNumberHandler = (input)->{
            try{
                if (iterationCounter.getAndIncrement()>=ITERATION_LIMIT-1){
                    log.warn("client process is shutting down >>> last iteration: {}",iterationCounter.get()-1);
                    queueingThread.shutdown();
                }
                int inputLocal = input.get();
                if (inputLocal>acceptedValueFromServer.get()){
                    acceptedValueFromServer.set(inputLocal);
                }else {
                    acceptedValueFromServer.set(-1);
                }
                int acceptedLocal = acceptedValueFromServer.get();
                log.info("server current value: {}",acceptedLocal != -1 ? acceptedLocal : "<not available!...>");
                log.info("client current value: {}",clientValue.getAndIncrement());
                clientValue.addAndGet(acceptedLocal != -1 ? acceptedLocal : 0);
                log.info("ITERATION RESULT >>> {}",clientValue.get());
            }catch (Exception e){
                log.error("error during client process!...", e);
                queueingThread.shutdown();
            }
        };
        queueingThread.scheduleAtFixedRate(()-> clientSequenceNumberHandler.accept(generatedServerNumber),0,ITERATION_INTERVAL, TimeUnit.SECONDS);
    }
}

package ru.otus.services.processors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.api.SensorDataProcessor;
import ru.otus.api.model.SensorData;
import ru.otus.lib.SensorDataBufferedWriter;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.locks.ReentrantLock;

public class SensorDataProcessorBuffered implements SensorDataProcessor {

    private static final Logger LOG = LoggerFactory.getLogger(SensorDataProcessorBuffered.class);

    private final int bufferSize;
    private final SensorDataBufferedWriter writer;
    private final BlockingQueue<SensorData> dataBuffer;
    private final ReentrantLock lock = new ReentrantLock();
    private final Comparator<SensorData> comparator = Comparator.comparing(SensorData::getMeasurementTime);

    public SensorDataProcessorBuffered(int bufferSize, SensorDataBufferedWriter writer) {
        this.bufferSize = bufferSize;
        this.writer = writer;
        dataBuffer = new ArrayBlockingQueue<>(bufferSize);
    }

    @Override
    public void process(SensorData data) {
        boolean elementCanBeInserted = dataBuffer.offer(data);
        if (dataBuffer.size() >= bufferSize || !elementCanBeInserted){
            flush();
        }
    }

    public void flush() {
        lock.lock();
        try {
            List<SensorData> flushed = new ArrayList<>();
            dataBuffer.drainTo(flushed);
            if (flushed.isEmpty())return;
            writer.writeBufferedData(flushed.stream().sorted(comparator).toList());
        } catch (Exception e) {
            LOG.error("Ошибка в процессе записи буфера", e);
        }finally {
            lock.unlock();
        }
    }

    @Override
    public void onProcessingEnd() {
        flush();
    }
}

package ru.otus.processor.homework;

import ru.otus.model.Message;
import ru.otus.processor.Processor;


public class ProcessorThrowsExceptionEveryTwoSecond implements Processor{

    private final TimeProvider errorTime;

    public ProcessorThrowsExceptionEveryTwoSecond(TimeProvider currentRunTimeSecond) {
        this.errorTime = currentRunTimeSecond;
    }

    @Override
    public Message process(Message message) throws RuntimeException {
        long errorSecond = errorTime.getTime().getSecond();
        if (errorSecond%2==0){
            throw new RuntimeException(String.format("### error call on %s second! ###%n", errorSecond));
        }
        return message;
    }
}


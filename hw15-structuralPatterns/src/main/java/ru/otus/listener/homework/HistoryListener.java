package ru.otus.listener.homework;

import ru.otus.listener.Listener;
import ru.otus.model.Message;

import java.util.*;

public class HistoryListener implements Listener, HistoryReader {

    private final Map<Long, Message> messageHistory;

    public HistoryListener() {
        this.messageHistory = new HashMap<>();
    }

    @Override
    public void onUpdated(Message msg) {
        if (messageHistory.get(msg.getId())==null){
            Message messageToAchieve = msg.clone();
            messageHistory.put(messageToAchieve.getId(), messageToAchieve);
            System.out.println("message successfully added!");
        }else {
            System.out.printf("message with id %d already exists in archive!%n", msg.getId());
        }
    }

    @Override
    public Optional<Message> findMessageById(long id) {
        return Optional.of(messageHistory.get(id));
    }

    public void printArchiveMessages(){
        System.out.println("\nArchive messages:");
        messageHistory.forEach((key, value) -> System.out.println("message data: " + key + " => \n" + value));
    }
}

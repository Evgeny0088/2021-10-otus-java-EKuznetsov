package ru.otus.listener.homework;

import ru.otus.listener.Listener;
import ru.otus.model.Message;
import ru.otus.model.ObjectForMessage;

import java.util.*;

public class HistoryListener implements Listener, HistoryReader {

    private final Map<Long, Message> messageHistory;

    public HistoryListener() {
        this.messageHistory = new HashMap<>();
    }

    @Override
    public void onUpdated(Message msg) {
        try{
            if (messageHistory.get(msg.getId())==null){
                ObjectForMessage objectForMessage = new ObjectForMessage();
                if (msg.getField13()==null){
                    objectForMessage = null;
                }else {
                    objectForMessage.setData(msg.getField13().getData()!=null ?
                            new ArrayList<>(msg.getField13().getData()) : new ArrayList<>());
                }
                Message messageToAchieve = new Message.Builder(msg.getId())
                        .field1(msg.getField1()).field2(msg.getField2())
                        .field3(msg.getField3()).field4(msg.getField4())
                        .field5(msg.getField5()).field6(msg.getField6())
                        .field7(msg.getField7()).field8(msg.getField8())
                        .field9(msg.getField9()).field10(msg.getField10())
                        .field11(msg.getField11()).field12(msg.getField12())
                        .field13(objectForMessage)
                        .build();
                messageHistory.put(msg.getId(), messageToAchieve);
                System.out.println("message successfully added!");
            }else {
                System.out.printf("message with id %d already exists in archive!%n", msg.getId());
            }
        }catch (UnsupportedOperationException exception){
            throw new UnsupportedOperationException();
        }
    }

    @Override
    public Optional<Message> findMessageById(long id) {
        try {
            return Optional.of(messageHistory.get(id));
        }catch (UnsupportedOperationException exception){
            throw new UnsupportedOperationException();
        }
    }

    public void printArchiveMessages(){
        System.out.println("\nArchive messages:");
        messageHistory.forEach((key, value) -> System.out.println("message data: " + key + " => \n" + value));
    }
}

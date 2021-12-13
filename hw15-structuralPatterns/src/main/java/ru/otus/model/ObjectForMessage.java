package ru.otus.model;

import java.util.ArrayList;
import java.util.List;

public class ObjectForMessage implements Cloneable {
    private List<String> data;

    public List<String> getData() {
        return data;
    }

    public void setData(List<String> data) {
        this.data = data;
    }

    @Override
    protected ObjectForMessage clone() {
        ObjectForMessage objectForMessage = new ObjectForMessage();
        objectForMessage.setData(data!=null ?
                new ArrayList<>(data) : new ArrayList<>());
        return objectForMessage;
    }

    @Override
    public String toString() {
        return data!=null ? "ObjectForMessage{" +
                "data=" + data +
                '}'
                : "ObjectForMessage{" +
                "data=" + "[]" +
                '}';
    }
}

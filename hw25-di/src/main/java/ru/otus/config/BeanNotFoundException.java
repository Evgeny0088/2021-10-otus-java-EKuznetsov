package ru.otus.config;

public class BeanNotFoundException extends RuntimeException{
    public BeanNotFoundException(String message, RuntimeException exception) {
        super(message,exception.getCause());
    }
}

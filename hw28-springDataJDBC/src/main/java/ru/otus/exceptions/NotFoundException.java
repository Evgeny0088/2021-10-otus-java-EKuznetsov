package ru.otus.exceptions;

public class NotFoundException extends RuntimeException {
    public<T> NotFoundException(Long id, T item ) {
        super(String.format("%s с id:%d не найден", item, id));
    }
}

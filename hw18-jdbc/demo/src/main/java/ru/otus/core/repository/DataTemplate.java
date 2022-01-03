package ru.otus.core.repository;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface DataTemplate<T> {
    Optional<T> findById(Connection connection, long id) throws SQLException;

    List<T> findAll(Connection connection) throws SQLException;

    long insert(Connection connection, T object);

    void update(Connection connection, T object);
}

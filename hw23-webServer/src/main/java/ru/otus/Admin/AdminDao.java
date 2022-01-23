package ru.otus.Admin;

import java.util.Optional;

public interface AdminDao {

    Optional<Admin> findById(long id);
    Optional<Admin> findByLogin(String login);
}
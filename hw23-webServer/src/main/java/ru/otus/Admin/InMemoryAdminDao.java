package ru.otus.Admin;

import java.util.*;

public class InMemoryAdminDao implements AdminDao {

    private final Map<Long, Admin> admins;

    public InMemoryAdminDao() {
        admins = new HashMap<>();
        admins.put(1L, new Admin(1L, "admin", "admin1", "admin1"));
    }

    @Override
    public Optional<Admin> findById(long id) {
        return Optional.ofNullable(admins.get(id));
    }

    @Override
    public Optional<Admin> findByLogin(String login) {
        return admins.values().stream().filter(v -> v.getLogin().equals(login)).findFirst();
    }
}

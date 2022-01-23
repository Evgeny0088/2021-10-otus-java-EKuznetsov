package ru.otus.services;

import ru.otus.Admin.AdminDao;

public class UserAuthServiceImpl implements UserAuthService {

    private final AdminDao admins;

    public UserAuthServiceImpl(AdminDao admins) {
        this.admins = admins;
    }

    @Override
    public boolean authenticate(String login, String password) {
        return admins.findByLogin(login)
                .map(user -> user.getPassword().equals(password))
                .orElse(false);
    }
}

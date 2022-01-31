package ru.otus.services;

import ru.otus.user.UserDao;

public class UserAuthServiceImpl implements UserAuthService {

    private final UserDao admins;

    public UserAuthServiceImpl(UserDao admins) {
        this.admins = admins;
    }

    @Override
    public boolean authenticate(String login, String password) {
        return admins.findByLogin(login)
                .map(user -> user.getPassword().equals(password) && user.isAdmin())
                .orElse(false);
    }
}

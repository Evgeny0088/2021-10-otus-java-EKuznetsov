package ru.otus.User;

public class User {

    private final long id;
    private final String name;
    private final String login;
    private final String password;
    private boolean isAdmin;

    public User(long id, String name, String login, String password, boolean isAdmin) {
        this.id = id;
        this.name = name;
        this.login = login;
        this.password = password;
        this.isAdmin = isAdmin;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }


    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

}

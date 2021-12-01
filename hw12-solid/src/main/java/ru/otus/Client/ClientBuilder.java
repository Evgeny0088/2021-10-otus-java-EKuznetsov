package ru.otus.Client;

import java.util.UUID;

public class ClientBuilder {
    public static ClientImpl clientBuilder(){
        ClientImpl client = new ClientImpl(UUID.randomUUID().toString());
        client.setAccountIsActive(true);
        return client;
    }
}

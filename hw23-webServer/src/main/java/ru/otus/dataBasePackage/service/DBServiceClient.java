package ru.otus.dataBasePackage.service;

import ru.otus.dataBasePackage.models.Client;

import java.util.List;
import java.util.Optional;

public interface DBServiceClient {

    Client saveClient(Client client);

    Optional<Client> getClient(long id);

    List<Client> findAll();

    String deleteClient(Client client);

}

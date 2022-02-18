package ru.otus.service;

import ru.otus.models.Client;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface ClientService {

    Client saveClient(Client client);

    Optional<Client> getClient(Long id);

    List<Client> findAll();

    String deleteClient(Client client);

    String updateClient(Map<String, String> form, HttpServletRequest request, Long id);
}

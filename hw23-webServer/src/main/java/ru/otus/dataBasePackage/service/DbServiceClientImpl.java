package ru.otus.dataBasePackage.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.dataBasePackage.Cache.MyCache;
import ru.otus.dataBasePackage.models.Client;
import ru.otus.dataBasePackage.repository.DataTemplate;
import ru.otus.dataBasePackage.sessionmanager.TransactionManager;

import java.util.List;
import java.util.Optional;

public class DbServiceClientImpl implements DBServiceClient {
    private static final Logger log = LoggerFactory.getLogger(DbServiceClientImpl.class);

    private final DataTemplate<Client> clientDataTemplate;
    private final TransactionManager transactionManager;

    private final MyCache<String,Client> clientCache;


    public DbServiceClientImpl(TransactionManager transactionManager, DataTemplate<Client> clientDataTemplate, MyCache<String, Client> clientCache) {
        this.transactionManager = transactionManager;
        this.clientDataTemplate = clientDataTemplate;
        this.clientCache = clientCache;
    }

    @Override
    public Client saveClient(Client client) {
        return transactionManager.doInTransaction(session -> {
            var clientCloned = client.clone();
            if (client.getId() == null) {
                clientDataTemplate.insert(session, clientCloned);
                log.info("created client: {}", clientCloned);
                return clientCloned;
            }
            clientDataTemplate.update(session, clientCloned);
            clientCache.put(String.valueOf(clientCloned.getId()),clientCloned);
            log.info("updated client: {}", clientCloned);
            return clientCloned;
        });
    }

    @Override
    public Optional<Client> getClient(long id) {
        Optional<Client> cacheClient = Optional.ofNullable(clientCache.get(String.valueOf(id)));
        return cacheClient.isPresent() ? cacheClient
                : transactionManager.doInReadOnlyTransaction(session -> {
            var clientOptional = clientDataTemplate.findById(session, id);
            clientOptional.ifPresent(client -> clientCache.put(String.valueOf(client.getId()),client));
            log.info("client: {}", clientOptional);
            return clientOptional;
        });
    }

    @Override
    public List<Client> findAll() {
        return transactionManager.doInReadOnlyTransaction(session -> {
            var clientList = clientDataTemplate.findAll(session);
            if (!clientList.isEmpty()){
                log.info("### cache updating data: ###");
                clientList.forEach(client-> clientCache.put(String.valueOf(client.getId()),client));
            }
            log.info("clientList:{}", clientList);
            return clientList;
        });
    }

    @Override
    public String deleteClient(Client client) {
        return transactionManager.doInTransaction(session -> {
            long id = client.getId() != null ? client.getId() : 0;
            var clientToDelete = clientDataTemplate.findById(session,id).orElse(null);
            if (clientToDelete != null){
                clientDataTemplate.delete(session,clientToDelete);
                clientCache.remove(String.valueOf(id));
                log.info("client with id:{} is removed!",id);
            }else {
                log.warn("client not found, failed to remove!");
            }
            return String.valueOf(id);
        });
    }
}

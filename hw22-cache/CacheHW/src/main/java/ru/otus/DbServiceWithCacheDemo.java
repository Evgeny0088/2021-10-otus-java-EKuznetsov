package ru.otus;

import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.cachehw.DbServiceClientWithCacheImpl;
import ru.otus.cachehw.MyCache;
import ru.otus.core.repository.DataTemplateHibernate;
import ru.otus.core.repository.HibernateUtils;
import ru.otus.core.sessionmanager.TransactionManagerHibernate;
import ru.otus.crm.dbmigrations.MigrationsExecutorFlyway;
import ru.otus.crm.model.Address;
import ru.otus.crm.model.Client;
import ru.otus.crm.model.Phone;

import java.util.List;

public class DbServiceWithCacheDemo {

    private static final Logger log = LoggerFactory.getLogger(DbServiceWithCacheDemo.class);

    public static final String HIBERNATE_CFG_FILE = "hibernate.cfg.xml";

    public static void main(String[] args) {
        var configuration = new Configuration().configure(HIBERNATE_CFG_FILE);

        var dbUrl = configuration.getProperty("hibernate.connection.url");
        var dbUserName = configuration.getProperty("hibernate.connection.username");
        var dbPassword = configuration.getProperty("hibernate.connection.password");

        new MigrationsExecutorFlyway(dbUrl, dbUserName, dbPassword).executeMigrations();

        Class<?>[] entityClasses = {Client.class, Address.class, Phone.class};

        var sessionFactory = HibernateUtils.buildSessionFactory(configuration, entityClasses);

        var transactionManager = new TransactionManagerHibernate(sessionFactory);
///
        var clientTemplate = new DataTemplateHibernate<>(Client.class);
///
        var cache = new MyCache<String, Client>();

        var dbServiceClient = new DbServiceClientWithCacheImpl(transactionManager, clientTemplate, cache);
        dbServiceClient.saveClient(new Client("dbServiceFirst"));

        var clientSecond = dbServiceClient.saveClient(new Client("dbServiceSecond"));
        var clientSecondSelected = dbServiceClient.getClient(clientSecond.getId())
                .orElseThrow(() -> new RuntimeException("Client not found, id:" + clientSecond.getId()));
        log.info("clientSecondSelected:{}", clientSecondSelected);
///
        dbServiceClient.saveClient(new Client(clientSecondSelected.getId(), "dbServiceSecondUpdated",
                        new Address(null,"AnyStreet"), List.of(new Phone(null,"13-555-22"))));

        var clientUpdated = dbServiceClient.getClient(clientSecondSelected.getId())
                .orElseThrow(() -> new RuntimeException("Client not found, id:" + clientSecondSelected.getId()));
        log.info("clientUpdated:{}", clientUpdated);

        log.info("### get all clients:");
        dbServiceClient.findAll().forEach(client -> log.info("client:{}", client));
    }
}

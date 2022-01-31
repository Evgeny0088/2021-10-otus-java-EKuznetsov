package ru.otus.dataBasePackage.hibernateSetup.config;

import org.hibernate.cfg.Configuration;
import ru.otus.dataBasePackage.Cache.MyCache;
import ru.otus.dataBasePackage.repository.DataTemplateHibernate;
import ru.otus.dataBasePackage.sessionmanager.TransactionManagerHibernate;
import ru.otus.dataBasePackage.hibernateSetup.dbmigrations.MigrationsExecutorFlyway;
import ru.otus.dataBasePackage.models.Address;
import ru.otus.dataBasePackage.models.Client;
import ru.otus.dataBasePackage.models.Phone;
import ru.otus.dataBasePackage.service.DbServiceClientImpl;

public class HibernateSetup {

    public static final String HIBERNATE_CFG_FILE = "hibernate/hibernate.cfg.xml";

    public static DbServiceClientImpl createDbServiceClient() {
        var configuration = new Configuration().configure(HIBERNATE_CFG_FILE);
        var dbUrl = configuration.getProperty("hibernate.connection.url");
        var dbUserName = configuration.getProperty("hibernate.connection.username");
        var dbPassword = configuration.getProperty("hibernate.connection.password");

        new MigrationsExecutorFlyway(dbUrl, dbUserName, dbPassword).executeMigrations();

        Class<?>[] entityClasses = {Client.class, Address.class, Phone.class};
        MyCache<String, Client> clientCache = new MyCache<>();

        var sessionFactory = HibernateUtils.buildSessionFactory(configuration, entityClasses);

        var transactionManager = new TransactionManagerHibernate(sessionFactory);

        var clientTemplate = new DataTemplateHibernate<>(Client.class);


        return new DbServiceClientImpl(transactionManager, clientTemplate, clientCache);
    }
}

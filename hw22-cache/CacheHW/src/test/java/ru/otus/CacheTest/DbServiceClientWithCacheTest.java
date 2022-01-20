package ru.otus.CacheTest;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.TestPreparation.AbstractHibernateTest;
import ru.otus.crm.model.Client;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@DisplayName("Проверка скорости кэша данных из базы данных ")
class DbServiceClientWithCacheTest extends AbstractHibernateTest {

    private static final Logger logger = LoggerFactory.getLogger(DbServiceClientWithCacheTest.class);

    @Test
    @DisplayName("к элементам напрямую из запроса к базе и через кэш")
    void loadingTimeCheck() {

         // create list for clients from DB
        List<Client> clients = new ArrayList<>();

        // firstly we get elements from directly from DB ##################
        // during this operation cache is being filled with clients from DB!
        logger.info("##### Loading clients from DB: #####");
        long measuredTimeForDB = fetchTime(clients);
        clients.forEach(System.out::println);
        // cleanup client list #############################################
        clients.clear();
        System.out.println("\n");
        logger.info("##### clients list is cleaned before loading clients from cache #####\n");

        // next we get elements from cache #################################
        logger.info("##### Loading clients from cache: #####");
        long measuredTimeForCache = fetchTime(clients);
        clients.forEach(System.out::println);
        assertThat(measuredTimeForDB).isGreaterThan(measuredTimeForCache);
    }

    private long fetchTime(List<Client> clients){
        long before = System.currentTimeMillis();
        for (int i = 1; i<11; i++){ // we inserted 10 elements into db, starting from 1 index
            Optional<Client> client = dbServiceClient.getClient(i);
            client.ifPresent(clients::add);
        }
        long measuredTime = System.currentTimeMillis()-before;
        logger.info("loading time:{} milli seconds", measuredTime);
        return measuredTime;
    }
}
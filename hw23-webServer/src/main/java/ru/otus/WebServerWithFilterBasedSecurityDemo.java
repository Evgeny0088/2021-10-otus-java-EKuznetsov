package ru.otus;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import ru.otus.user.UserDao;
import ru.otus.user.InMemoryUserDao;
import ru.otus.dataBasePackage.hibernateSetup.config.HibernateSetup;
import ru.otus.dataBasePackage.service.DbServiceClientImpl;
import ru.otus.server.UsersWebServer;
import ru.otus.server.UsersWebServerWithFilterBasedSecurity;
import ru.otus.services.TemplateProcessor;
import ru.otus.services.TemplateProcessorImpl;
import ru.otus.services.UserAuthService;
import ru.otus.services.UserAuthServiceImpl;

public class WebServerWithFilterBasedSecurityDemo {
    private static final int WEB_SERVER_PORT = 8080;
    private static final String TEMPLATES_DIR ="/templates/";

    public static void main(String[] args) throws Exception {
        UserDao users = new InMemoryUserDao();
        DbServiceClientImpl dbServiceClient = HibernateSetup.createDbServiceClient();
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation()
                                        .serializeNulls().setPrettyPrinting().create();
        TemplateProcessor templateProcessor = new TemplateProcessorImpl(TEMPLATES_DIR);
        UserAuthService authService = new UserAuthServiceImpl(users);

        UsersWebServer usersWebServer = new UsersWebServerWithFilterBasedSecurity(WEB_SERVER_PORT,
                authService, gson, dbServiceClient, templateProcessor);

        usersWebServer.start();
        usersWebServer.join();
    }
}

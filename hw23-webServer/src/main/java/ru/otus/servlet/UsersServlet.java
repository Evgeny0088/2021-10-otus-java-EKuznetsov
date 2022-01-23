package ru.otus.servlet;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.otus.dataBasePackage.models.Address;
import ru.otus.dataBasePackage.models.Client;
import ru.otus.dataBasePackage.models.Phone;
import ru.otus.dataBasePackage.service.DBServiceClient;
import ru.otus.services.TemplateProcessor;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static jakarta.servlet.http.HttpServletResponse.SC_BAD_REQUEST;
import static jakarta.servlet.http.HttpServletResponse.SC_OK;

public class UsersServlet extends HttpServlet {

    private static final String USERS_PAGE_TEMPLATE = "users.html";
    private static final String CLIENTS = "clients";
    private static final String CLIENT_LIST_EMPTY = "message";
    private static final String ERROR_CLIENT_CREATION = "error_client";

    private final DBServiceClient serviceClient;
    private final TemplateProcessor templateProcessor;

    public UsersServlet(TemplateProcessor templateProcessor, DBServiceClient serviceClient) {
        this.templateProcessor = templateProcessor;
        this.serviceClient = serviceClient;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse response) throws IOException {

        // client removal handling
        String queryParam = req.getParameterValues("id")!=null ? req.getParameterValues("id")[0] : ""; // check if query id is exists
        long clientId = !queryParam.isBlank() ? Long.parseLong(queryParam) : 0; // if id is not empty
        var clientToRemove = serviceClient.getClient(clientId).orElse(null); // check if client with such id is exists
        if (clientId>0 && clientToRemove != null){
            serviceClient.deleteClient(clientToRemove); // remove if exists
            response.setStatus(SC_OK);
            response.sendRedirect("/users");
        }
        // client removal handling

        // show all clients or message if client list empty
        Map<String, Object> paramsMap = new HashMap<>();
        List<Client> clients = serviceClient.findAll();
        String message = clients.isEmpty() ? "список пуст, добавь клиентов!" : "";
        paramsMap.put(CLIENTS, clients);
        paramsMap.put(CLIENT_LIST_EMPTY,message);
        paramsMap.put(ERROR_CLIENT_CREATION, req.getSession().getAttribute(ERROR_CLIENT_CREATION));
        response.setContentType("text/html");
        response.getWriter().println(templateProcessor.getPage(USERS_PAGE_TEMPLATE, paramsMap));
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String name = req.getParameter("name");
        String address = req.getParameter("address");
        String phone = req.getParameter("phone");

        // check if client name is blank then throw error message, else create new client
        if (name.isBlank()){
            resp.setStatus(SC_BAD_REQUEST);
            req.getSession().setAttribute(ERROR_CLIENT_CREATION, "заполните корректно имя клиета!");
            resp.sendRedirect("/users");
        }else {
            Client newClient = new Client(null,name,
                    !address.isBlank() ? new Address(null,address) : null,
                    !phone.isBlank() ? List.of(new Phone(null, phone)) : Collections.emptyList());
            serviceClient.saveClient(newClient);
            resp.setStatus(SC_OK);
            req.getSession().setAttribute(ERROR_CLIENT_CREATION,null);
            resp.sendRedirect("/users");
        }
    }
}

package ru.otus.servlet;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import ru.otus.services.TemplateProcessor;
import ru.otus.services.UserAuthService;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static jakarta.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;

public class LoginServlet extends HttpServlet {

    private static final int MAX_INACTIVE_INTERVAL = 60;
    private static final String PARAM_LOGIN = "login";
    private static final String PARAM_PASSWORD = "password";
    private static final String LOGIN_PAGE_TEMPLATE = "index.html";
    private static final String LOGIN_ERROR = "login_error"; // этот параметер не работает на странице, т.к. freemarker не работает на index.html

    private final TemplateProcessor templateProcessor;
    private final UserAuthService userAuthService;

    public LoginServlet(TemplateProcessor templateProcessor, UserAuthService userAuthService) {
        this.userAuthService = userAuthService;
        this.templateProcessor = templateProcessor;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");
        Map<String, Object> viewParams = new HashMap<>();
        viewParams.put(LOGIN_ERROR, request.getSession().getAttribute(LOGIN_ERROR));
        response.getWriter().println(templateProcessor.getPage(LOGIN_PAGE_TEMPLATE, viewParams));
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String name = request.getParameter(PARAM_LOGIN);
        String password = request.getParameter(PARAM_PASSWORD);
        if (userAuthService.authenticate(name, password)) {
            HttpSession session = request.getSession();
            session.setMaxInactiveInterval(MAX_INACTIVE_INTERVAL);
            response.sendRedirect("/users");
        } else {
            response.setStatus(SC_UNAUTHORIZED);
            request.getSession().setAttribute(LOGIN_ERROR,"логин или пароль неверны!");
            response.sendRedirect("/");
        }
    }
}

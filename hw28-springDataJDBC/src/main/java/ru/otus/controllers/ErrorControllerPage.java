package ru.otus.controllers;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

@Controller
public class ErrorControllerPage implements ErrorController {
    @RequestMapping("/error")
    public String ErrorHandler(HttpServletRequest request){
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        if (status!=null){
            int statusCode = Integer.parseInt(status.toString());
            if (statusCode==404){
                return "error-404";
            }
            else {
                return "error-500";
            }
        }
        return "error";
    }
}

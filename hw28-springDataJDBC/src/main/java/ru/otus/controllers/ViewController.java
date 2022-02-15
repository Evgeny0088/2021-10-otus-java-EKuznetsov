package ru.otus.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.otus.exceptions.NotFoundException;
import ru.otus.models.Client;
import ru.otus.service.ClientService;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/hw-springJDBC/homepage")
public class ViewController {

    private static final String ERROR_CLIENT = "error_client";
    private static final String CLIENT_STATUS = "client_status";
    private static final String INFO_MESSAGE = "message";

    @Autowired
    private ClientService clientService;

    @GetMapping()
    public String getAllClients(@RequestParam(value = "client_id",required = false) Long client_id,
                                HttpServletRequest request,
                                Model model){
        String message = "";
        List<Client> clients = new ArrayList<>();
        if (client_id == null){
            clients = clientService.findAll();
            message = clients.isEmpty() ? "список пуст, добавь клиентов!" : "";
        }else {
            Client searchedClient = clientService.getClient(client_id).orElse(null);
            if (searchedClient != null){
                clients.add(searchedClient);
            }else {
                message = "клиент не найден!";
            }
        }
        model.addAttribute("clients",clients);
        model.addAttribute(INFO_MESSAGE, message);
        model.addAttribute(ERROR_CLIENT,request.getSession().getAttribute(ERROR_CLIENT));
        model.addAttribute(CLIENT_STATUS,request.getSession().getAttribute(CLIENT_STATUS));
        request.getSession().setAttribute(CLIENT_STATUS,null);
        request.getSession().setAttribute(ERROR_CLIENT,null);
        return "users";
    }

    @PostMapping()
    public String createNewClient(@RequestParam Map<String, String> form,
                                  HttpServletRequest request, RedirectAttributes model){
        String message = clientService.updateClient(form, request, null);
        model.addAttribute(ERROR_CLIENT,message);
        return "redirect:/hw-springJDBC/homepage";
    }

    @GetMapping("/edit/{client_id}")
    public String editClient(@PathVariable("client_id") Long id,
                             HttpServletRequest request, Model model){
        Optional<Client> client = clientService.getClient(id);
        String name = client.isPresent() ? client.get().getName() : "";
        String address = client.isPresent() ? client.get().getAddress().getStreet() : "";
        String phone = "";
        if (client.isPresent() && !client.get().getClientPhones().isEmpty()){
            phone = client.get().getClientPhones().iterator().next().getNumber();
        }
        model.addAttribute("name",name);
        model.addAttribute("address",address);
        model.addAttribute("phone",phone);
        model.addAttribute(ERROR_CLIENT,request.getSession().getAttribute(ERROR_CLIENT));
        return "editClient";
    }

    @PostMapping("/edit/{client_id}")
    public String editClientPost(@PathVariable("client_id") Long id,
                                 @RequestParam Map<String, String> form,
                                 HttpServletRequest request, Model model){
        String message = clientService.updateClient(form,request,id);
        model.addAttribute(ERROR_CLIENT,message);
        if (message == null){
            String clientStatus = String.format("клиент с id <%d> обновлен!",id);
            request.getSession().setAttribute(CLIENT_STATUS,clientStatus);
            model.addAttribute(CLIENT_STATUS,clientStatus);
            return "redirect:/hw-springJDBC/homepage";
        }else {
            return "editClient";
        }
    }

    @RequestMapping(method = RequestMethod.GET,value = "/delete/{client_id}")
    public String deleteQuestion(@PathVariable("client_id") Long id,
                                 HttpServletRequest request, RedirectAttributes model) throws NotFoundException {
        Optional<Client> client = clientService.getClient(id);
        if (client.isEmpty()) {
            request.getSession().setAttribute(CLIENT_STATUS,null);
            throw new NotFoundException(id,"client");
        }
        clientService.deleteClient(client.get());
        String message = String.format("клиент с id <%d> удален!",id);
        request.getSession().setAttribute(CLIENT_STATUS,message);
        model.addAttribute(CLIENT_STATUS,message);
        return "redirect:/hw-springJDBC/homepage";
    }
}

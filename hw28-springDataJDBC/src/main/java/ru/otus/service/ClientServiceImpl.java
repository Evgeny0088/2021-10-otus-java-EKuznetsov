package ru.otus.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.otus.models.Address;
import ru.otus.models.Client;
import ru.otus.models.Phone;
import ru.otus.repository.ClientRepo;
import ru.otus.repository.DataTemplateException;
import ru.otus.sessionmanager.TransactionManager;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Service
public class ClientServiceImpl implements ClientService {
    private static final Logger log = LoggerFactory.getLogger(ClientServiceImpl.class);

    private final TransactionManager transactionManager;
    private final ClientRepo clientRepository;
    private final AddressService addressService;
    private final PhoneService phoneService;

    @Autowired
    public ClientServiceImpl(TransactionManager transactionManager, ClientRepo clientRepository,
                             AddressService addressService, PhoneService phoneService) {
        this.transactionManager = transactionManager;
        this.clientRepository = clientRepository;
        this.addressService = addressService;
        this.phoneService = phoneService;
    }

    @Override
    public Client saveClient(Client client) {
        return transactionManager.doInTransaction(() -> {
            try{
                Client savedClient = clientRepository.save(client);
                log.info("saved client: {}", savedClient);
                return savedClient;
            }catch (RuntimeException exception){
                throw new DataTemplateException(exception);
            }
        });
    }

    @Override
    public Optional<Client> getClient(Long id) {
        return transactionManager.doInReadOnlyTransaction(()->{
            var clientOptional = clientRepository.findById(id);
            log.info("client: {}", clientOptional);
            return clientOptional;
        });
    }

    @Override
    public List<Client> findAll() {
        return transactionManager.doInReadOnlyTransaction(()->{
            var clientList = new ArrayList<>(clientRepository.findAll());
            log.info("clientList:{}", clientList);
            return clientList;
        });
    }

    @Override
    public String deleteClient(Client client) {
        return transactionManager.doInTransaction(()-> {
            Long id = client.getId() != null ? client.getId() : -1L;
            var clientToDelete = clientRepository.findById(id).orElse(null);
            if (clientToDelete != null){
                clientRepository.delete(clientToDelete);
                log.warn("client <{}> successfully deleted!",id);
            }else {
                log.warn("client not found, failed to remove!");
            }
            return String.valueOf(id);
        });
    }

    @Override
    public String updateClient(Map<String, String> form, HttpServletRequest request, Long id){
        String name = form.get("name");
        String address = form.get("address");
        String phone = form.get("phone");
        Address newAddress = null;
        Phone newPhone;
        String message;
        Set<Phone> phones = new HashSet<>();
        if (name.isBlank()){
            message = "заполните корректно имя клиента!";
            request.getSession().setAttribute("error_client",message);
            return message;
        }
        Client updatedClient = getClient(id).orElse(null);
        Address updatedAddress = addressService.getAddressByStreet(address).orElse(null);
        if (updatedAddress == null && !address.isBlank()){
            newAddress = addressService.saveAddress(new Address(null,address));
        }
        if (!phone.isBlank()){
            newPhone = new Phone(null,phone);
            phones.add(newPhone);
        }
        saveClient(new Client(updatedClient == null ? null : updatedClient.getId(),name,
                                updatedAddress == null ? newAddress : updatedAddress,phones));
        request.getSession().setAttribute("error_client", null);
        return null;
    }
}

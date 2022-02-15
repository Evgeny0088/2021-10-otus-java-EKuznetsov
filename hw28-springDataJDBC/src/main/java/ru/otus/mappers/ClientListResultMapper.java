package ru.otus.mappers;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import ru.otus.models.Address;
import ru.otus.models.Client;
import ru.otus.models.Phone;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class ClientListResultMapper implements ResultSetExtractor<List<Client>> {

    @Override
    public List<Client> extractData(ResultSet rs) throws SQLException, DataAccessException {
        String prevClientId = "";
        Client client = null;
        Phone phone;
        Address addr;
        var clientList = new ArrayList<Client>();
        Set<Phone> phoneList;
        while (rs.next()) {
            var client_id = rs.getString("client_id");
            var client_name = rs.getString("client_name");
            var client_address_id = rs.getString("address_id");
            var phone_id = rs.getString("phone_id");
            var phoneNumber = rs.getString("phone_number");
            var address = rs.getString("address");
            if (prevClientId.isBlank() || !prevClientId.equals(client_id)){
                phoneList = new HashSet<>();
                if (phone_id!=null){
                    phone = new Phone(Long.parseLong(phone_id),phoneNumber);
                    phoneList.add(phone);
                }
                if (client_address_id != null){
                    addr = new Address(Long.parseLong(client_address_id),address);
                    client = new Client(Long.parseLong(client_id), client_name, addr, phoneList);
                }else {
                    client = new Client(Long.parseLong(client_id),client_name,new Address(null,""),phoneList);
                }
                clientList.add(client);
            } else {
                if (client != null) client.getClientPhones().add(new Phone(null,phoneNumber));
            }
            prevClientId = client_id;
        }
        return clientList;
    }
}

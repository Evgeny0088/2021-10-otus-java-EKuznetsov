package ru.otus.mappers;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import ru.otus.models.Address;
import ru.otus.models.Client;
import ru.otus.models.Phone;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

public class ClientResultMapper implements ResultSetExtractor<Optional<Client>> {

    @Override
    public Optional<Client> extractData(ResultSet rs) throws SQLException, DataAccessException {
        String client_id = "";
        String client_name = "";
        String client_address_id;
        String phone_id;
        String phoneNumber;
        String address;
        Phone phone;
        Address addr = null;
        Set<Phone> phoneList = new HashSet<>();
        while (rs.next()) {
            client_id = rs.getString("client_id");
            client_name = rs.getString("client_name");
            client_address_id = rs.getString("address_id");
            phone_id = rs.getString("phone_id");
            phoneNumber = rs.getString("phone_number");
            address = rs.getString("address");
            if (phone_id!=null){
                phone = new Phone(Long.parseLong(phone_id),phoneNumber);
                phoneList.add(phone);
            }
            if (client_address_id!=null){
                addr = new Address(Long.parseLong(client_address_id),address);
            }
        }
        return client_id.isBlank() ? Optional.empty() :
                Optional.of(new Client(Long.parseLong(client_id), client_name,
                    Objects.requireNonNullElseGet(addr, () -> new Address(null,"")), phoneList));
    }
}

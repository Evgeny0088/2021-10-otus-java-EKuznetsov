package ru.otus.mappers;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import ru.otus.models.Address;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class AddressResultMapper implements ResultSetExtractor<Optional<Address>> {

    @Override
    public Optional<Address> extractData(ResultSet rs) throws SQLException, DataAccessException {
        String address_id = "";
        String street = "";
        while (rs.next()) {
            address_id = rs.getString("id");
            street = rs.getString("street");
        }
        return address_id.isBlank() ? Optional.empty() : Optional.of(new Address(Long.parseLong(address_id), street));
    }
}

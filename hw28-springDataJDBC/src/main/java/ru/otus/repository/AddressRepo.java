package ru.otus.repository;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.otus.mappers.AddressResultMapper;
import ru.otus.models.Address;

import java.util.Optional;

@Repository
public interface AddressRepo extends CrudRepository<Address, Long> {

    @Query(value = """
            select id, street from address where street=:street
        """,
        resultSetExtractorClass = AddressResultMapper.class)
    Optional<Address> findByStreet(@Param("street") String street);
}

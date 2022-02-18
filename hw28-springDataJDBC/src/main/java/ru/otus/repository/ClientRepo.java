package ru.otus.repository;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.otus.mappers.ClientListResultMapper;
import ru.otus.mappers.ClientResultMapper;
import ru.otus.models.Client;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClientRepo extends CrudRepository<Client, Long> {

    @Override
    @Query(value = """
            select c.id as client_id,
                   c.name as client_name,
                   c.client_address_id as address_id,
                   addr.street as address,
                   ph.phone_number as phone_number,
                   ph.id as phone_id
            from client c left outer join phones ph on c.id = ph.client_id
            left outer join address as addr on c.client_address_id=addr.id
            where c.id=:id
                            """,
            resultSetExtractorClass = ClientResultMapper.class)
    Optional<Client> findById(@Param("id") Long client_id);

    @Override
    @Query(value = """
            select c.id as client_id,
                   c.name as client_name,
                   c.client_address_id as address_id,
                   addr.street as address,              
                   ph.phone_number as phone_number,
                   ph.id as phone_id
            from client c left outer join phones ph on c.id = ph.client_id
            left outer join address as addr on c.client_address_id=addr.id order by c.id
                            """,
            resultSetExtractorClass = ClientListResultMapper.class)
    List<Client> findAll();
}

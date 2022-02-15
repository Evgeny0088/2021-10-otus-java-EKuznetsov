package ru.otus.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.otus.models.Phone;

import java.util.Optional;

@Repository
public interface PhoneRepo extends CrudRepository<Phone,Long> {

    Optional<Phone> findByAndNumber(String number);

}

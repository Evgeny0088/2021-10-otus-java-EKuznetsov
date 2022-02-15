package ru.otus.service;

import ru.otus.models.Phone;

import java.util.List;
import java.util.Optional;

public interface PhoneService {

    Phone savePhone(Phone phone);

    Optional<Phone> getPhone(long id);

    Optional<Phone> getByNumber(String number);

    List<Phone> findAll();

    String deletePhone(Phone phone);
}

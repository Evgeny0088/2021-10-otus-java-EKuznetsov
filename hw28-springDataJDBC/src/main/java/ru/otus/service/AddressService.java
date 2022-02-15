package ru.otus.service;

import ru.otus.models.Address;

import java.util.List;
import java.util.Optional;

public interface AddressService {

    Address saveAddress(Address address);

    Optional<Address> getAddrress(long id);

    Optional<Address> getAddressByStreet(String street);

    List<Address> findAll();

    String deleteAddress(Address address);
}

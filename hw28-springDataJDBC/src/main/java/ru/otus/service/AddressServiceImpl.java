package ru.otus.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.otus.models.Address;
import ru.otus.repository.AddressRepo;
import ru.otus.repository.DataTemplateException;
import ru.otus.sessionmanager.TransactionManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AddressServiceImpl implements AddressService {
    private static final Logger log = LoggerFactory.getLogger(AddressServiceImpl.class);

    private final TransactionManager transactionManager;
    private final AddressRepo addressRepository;

    public AddressServiceImpl(TransactionManager transactionManager, AddressRepo addressRepository) {
        this.transactionManager = transactionManager;
        this.addressRepository = addressRepository;
    }

    @Override
    public Address saveAddress(Address address) {
        return transactionManager.doInTransaction(() -> {
            try{
                Address savedAddress = addressRepository.save(address);
                log.info("saved address: {}", savedAddress);
                return savedAddress;
            }catch (RuntimeException exception){
                throw new DataTemplateException(exception);
            }
        });
    }

    @Override
    public Optional<Address> getAddrress(long id) {
        return transactionManager.doInReadOnlyTransaction(()->{
            var address = addressRepository.findById(id);
            log.info("address: {}", address);
            return address;
        });
    }

    @Override
    public Optional<Address> getAddressByStreet(String street) {
        return transactionManager.doInReadOnlyTransaction(()->{
            var address = addressRepository.findByStreet(street);
            log.info("address: {}", address);
            return address;
        });
    }

    @Override
    public List<Address> findAll() {
        var addressList = new ArrayList<Address>();
        addressRepository.findAll().forEach(addressList::add);
        log.info("addressList:{}", addressList);
        return addressList;
    }

    @Override
    public String deleteAddress(Address address) {
        return transactionManager.doInTransaction(()-> {
            long id = address.getId() != null ? address.getId() : -1;
            var addressToDelete = addressRepository.findById(id).orElse(null);
            if (addressToDelete != null){
                addressRepository.delete(addressToDelete);
            }else {
                log.warn("address not found, failed to remove!");
            }
            return String.valueOf(id);
        });
    }
}

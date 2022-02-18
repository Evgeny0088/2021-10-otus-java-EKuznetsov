package ru.otus.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.otus.models.Phone;
import ru.otus.repository.DataTemplateException;
import ru.otus.repository.PhoneRepo;
import ru.otus.sessionmanager.TransactionManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PhoneServiceImpl implements PhoneService {
    private static final Logger log = LoggerFactory.getLogger(PhoneServiceImpl.class);

    private final TransactionManager transactionManager;
    private final PhoneRepo phoneRepo;

    public PhoneServiceImpl(TransactionManager transactionManager, PhoneRepo phoneRepo) {
        this.transactionManager = transactionManager;
        this.phoneRepo = phoneRepo;
    }

    @Override
    public Phone savePhone(Phone phone) {
        return transactionManager.doInTransaction(() -> {
            try{
                Phone savedPhone = phoneRepo.save(phone);
                log.info("saved phone: {}", savedPhone);
                return savedPhone;
            }catch (RuntimeException exception){
                throw new DataTemplateException(exception);
            }
        });
    }

    @Override
    public Optional<Phone> getPhone(long id) {
        return transactionManager.doInReadOnlyTransaction(()->{
            Optional<Phone> phone = phoneRepo.findById(id);
            log.info("phone: {}", phone);
            return phone;
        });
    }

    @Override
    public List<Phone> findAll() {
        return transactionManager.doInReadOnlyTransaction(()->{
            var phoneList = new ArrayList<Phone>();
            phoneRepo.findAll().forEach(phoneList::add);
            log.info("phoneList:{}", phoneList);
            return phoneList;
        });
    }

    @Override
    public Optional<Phone> getByNumber(String number) {
        return transactionManager.doInReadOnlyTransaction(()->{
            Optional<Phone> phone = phoneRepo.findByAndNumber(number);
            log.info("phone: {}", phone);
            return phone;
        });
    }

    @Override
    public String deletePhone(Phone phone) {
        return transactionManager.doInTransaction(()-> {
            long id = phone.getId() != null ? phone.getId() : 0;
            var phoneToDelete = phoneRepo.findById(id).orElse(null);
            if (phoneToDelete != null){
                phoneRepo.delete(phoneToDelete);
            }else {
                log.warn("phone not found, failed to remove!");
            }
            return String.valueOf(id);
        });
    }
}

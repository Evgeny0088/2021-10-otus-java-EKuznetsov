package ru.otus;

import java.util.AbstractMap;
import java.util.Map;
import java.util.TreeMap;

public class CustomerService{

    private TreeMap<Customer, String> customers;

    public CustomerService(){
        this.customers = new TreeMap<>(Customer::compareTo);
    }

    public Map.Entry<Customer, String> getSmallest() {
        return new AbstractMap.SimpleEntry<>(customers.firstEntry());
    }

    public Map.Entry<Customer, String> getNext(Customer customer) {
        return new AbstractMap.SimpleEntry<>(customers.ceilingEntry(customer));
    }

    public void add(Customer customer, String data) {
        customers.put(customer,data);
    }
}

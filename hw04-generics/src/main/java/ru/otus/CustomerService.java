package ru.otus;

import java.util.Map;
import java.util.TreeMap;

public class CustomerService{

    private final TreeMap<Customer, String> customers;

    public CustomerService(){
        this.customers = new TreeMap<>(Customer::compareTo);
    }

    public Map.Entry<Customer, String> getSmallest() {
        return customers.firstEntry();
    }

    public Map.Entry<Customer, String> getNext(Customer customer) {
            if (customers.containsKey(customer)){
                return customers.ceilingEntry(new Customer(customer.getId(),customer.getName(), customer.getScores()+1));
            }
            else {
                return customers.ceilingEntry(customer);
            }
    }

    public void add(Customer customer, String data) {
        customers.put(customer,data);
    }
}

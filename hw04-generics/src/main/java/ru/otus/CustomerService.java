package ru.otus;

import java.util.AbstractMap;
import java.util.Map;
import java.util.TreeMap;

public class CustomerService{

    private final TreeMap<Customer, String> customers;

    public CustomerService() {
        this.customers = new TreeMap<>(Customer::compareTo);
    }

    public Map.Entry<Customer, String> getSmallest() {
        return copyOfCustomer(customers.firstEntry());
    }

    public Map.Entry<Customer, String> getNext(Customer customer){
            if (customers.containsKey(customer)){
                return copyOfCustomer(customers.ceilingEntry(new Customer(customer.getId(),
                        customer.getName(),customer.getScores()+1)));
            }
            else {return copyOfCustomer(customers.ceilingEntry(customer));}
    }

    public void add(Customer customer, String data) {
        customers.put(customer,data);
    }

    private Map.Entry<Customer,String> copyOfCustomer(Map.Entry<Customer, String> original){
        try {
            return new AbstractMap.SimpleEntry<>(new Customer(original.getKey().getId(),
                                                    original.getKey().getName(),
                                                    original.getKey().getScores()), original.getValue());
        }catch (NullPointerException e){return null;}
    }
}

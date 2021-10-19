package ru.otus;

import java.util.AbstractMap;
import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

public class CustomerService{

    private final TreeMap<Customer, String> customers;
    public CustomerService(){
        this.customers = new TreeMap<>(Comparator.comparingLong(Customer::getScores));
    }

    public Map.Entry<Customer, String> getSmallest() throws CloneNotSupportedException {
        return copyOfCustomer(customers.firstEntry());
    }

    public Map.Entry<Customer, String> getNext(Customer customer) throws CloneNotSupportedException {
            if (customers.containsKey(customer)){
                return copyOfCustomer(customers.ceilingEntry(new Customer(customer.getId(),
                        customer.getName(),customer.getScores()+1)));
            }
            else {return copyOfCustomer(customers.ceilingEntry(customer));}
    }

    public void add(Customer customer, String data) {
        customers.put(customer,data);
    }

    private Map.Entry<Customer,String> copyOfCustomer(Map.Entry<Customer, String> original) throws CloneNotSupportedException {
        try {
            return new AbstractMap.SimpleEntry<>((Customer) original.getKey().clone(), original.getValue());
        }catch (NullPointerException e){return null;}
    }
}

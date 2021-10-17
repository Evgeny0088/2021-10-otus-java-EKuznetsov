package ru.otus;

import java.util.LinkedHashSet;

public class CustomerReverseOrder {

    private LinkedHashSet<Customer> customers;

    public CustomerReverseOrder(){
        this.customers = new LinkedHashSet<>();
    }

    public void add(Customer customer) {
        customers.add(customer);
    }

    public Customer take() {
        Customer last;
        // in case if collection is empty from the beginning
        try {
            last = customers.stream().skip(customers.size()-1).findFirst().get();
        }catch (IllegalArgumentException e){
            System.out.println("no more elements in collection, therefore new generated element is returned!");
            return new Customer(0,"defualt name", 0);
        }
        customers.remove(last);
        return last;
    }
}

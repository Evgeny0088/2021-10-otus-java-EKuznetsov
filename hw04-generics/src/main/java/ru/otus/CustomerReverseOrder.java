package ru.otus;

import java.util.LinkedHashSet;
import java.util.Optional;

public class CustomerReverseOrder {

    private final LinkedHashSet<Customer> customers;

    public CustomerReverseOrder(){
        this.customers = new LinkedHashSet<>();
    }

    public void add(Customer customer) {
        customers.add(customer);
    }

    public Customer take() {
        Optional<Customer> last;
        try {
            last = customers.stream().skip(customers.size()-1).findFirst();
        }catch (IllegalArgumentException e){
            // in case if collection is empty from the beginning
            System.out.println("no more elements in collection, new generated element is returned!");
            return new Customer(0,"default name", 0);
        }
        last.ifPresent(customers::remove);
        return last.orElseGet(() -> new Customer(0, "default customer", 0));
    }
}

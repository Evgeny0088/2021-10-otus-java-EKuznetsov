package ru.otus;

import java.util.ArrayDeque;
import java.util.Deque;

public class CustomerReverseOrder {

    private final Deque<Customer> customers;

    public CustomerReverseOrder(){
        this.customers = new ArrayDeque<>();
    }

    public void add(Customer customer) {
        customers.push(customer);
    }

    public Customer take() {
        return customers.pop();
    }
}
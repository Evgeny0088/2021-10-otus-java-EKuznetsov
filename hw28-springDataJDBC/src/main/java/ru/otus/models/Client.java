package ru.otus.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.MappedCollection;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.lang.NonNull;

import java.util.*;

@Table(value = "client")
public class Client implements Persistable<Long> {

    @Id
    @Column(value = "id")
    private Long id;

    @Column(value = "name")
    @NonNull
    private String name;

    @Column(value = "client_address_id")
    private Long clientAddress_id;

    @MappedCollection(idColumn = "client_id")
    private Set<Phone> clientPhones;

    @Transient
    private final boolean isNew;

    @Transient
    private Address address;

    public Client(Long id, String name) {
        this(id, name,null,new HashSet<>());
    }

    @PersistenceConstructor
    public Client(Long id, @NonNull String name, Address address, Set<Phone> clientPhoneNumbers) {
        this.id = id;
        this.name = name;
        this.clientAddress_id = address != null ? address.getId(): null;
        this.address = address;
        this.isNew = id == null;
        assignClientForAddressIfExists();
        assignClientForEachPhoneNumberIfExists(clientPhoneNumbers);
    }

    public Long getClientAddress() {
        return clientAddress_id;
    }

    public void setClientAddress_id(Long clientAddress_id) {
        this.clientAddress_id = clientAddress_id;
    }

    public Set<Phone> getClientPhones() {
        return clientPhones;
    }

    public void setClientPhones(Set<Phone> clientPhones) {
        this.clientPhones = clientPhones;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getClientAddress_id() {
        return clientAddress_id;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    private void assignClientForEachPhoneNumberIfExists(Set<Phone> newPhones){
        clientPhones = newPhones == null ? new HashSet<>(): newPhones;
        clientPhones.forEach(phone -> phone.setClient_id(this.getId()));
    }

    private void assignClientForAddressIfExists(){
        if (address != null) address.setClient(this);
    }

    @Override
    public boolean isNew() {
        return isNew;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Client client = (Client) o;
        return Objects.equals(id, client.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Client{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}

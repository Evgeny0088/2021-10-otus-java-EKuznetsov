package ru.otus.crm.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "client")
public class Client implements Cloneable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "client_address_id")
    private Address clientAddress;

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    List<Phone> clientPhones;

    public Client() {
    }

    public Client(String name) {
        this.id = null;
        this.name = name;
    }

    public Client(Long id, String name, Address address, List<Phone> clientPhoneNumbers) {
        this.id = id;
        this.name = name;
        this.clientAddress = address;
        this.clientPhones = clientPhoneNumbers == null ? new ArrayList<>() : clientPhoneNumbers;
        assignClientForEachPhoneNumberIfExists();
        assignClientForAddressIfExists();
    }

    public Address getClientAddress() {
        return clientAddress;
    }

    public void setClientAddress(Address clientAddress) {
        this.clientAddress = clientAddress;
    }

    public List<Phone> getClientPhones() {
        return clientPhones;
    }

    public void setClientPhones(List<Phone> clientPhones) {
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

    private void assignClientForEachPhoneNumberIfExists(){
        if (!clientPhones.isEmpty()){
            clientPhones.forEach(phone -> phone.setClient(this));
        }
    }

    private void assignClientForAddressIfExists(){
        if (clientAddress != null){
            clientAddress.setClient(this);
        }
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
    public Client clone() {
        return new Client(this.id, this.name, this.clientAddress, this.clientPhones);
    }

    @Override
    public String toString() {
        return "Client{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}

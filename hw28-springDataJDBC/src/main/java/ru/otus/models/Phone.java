package ru.otus.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.lang.NonNull;

import java.util.Objects;

@Table(value = "phones")
public class Phone implements Persistable<Long> {

    @Id
    @Column(value = "id")
    private Long id;

    @Column(value = "phone_number")
    @NonNull
    private String number;

    @Column(value = "client_id")
    private Long client_id;

    @Transient
    private boolean isNew;

    public Phone(Long id, String number){
        this(id,number,null);
    }

    @PersistenceConstructor
    public Phone(Long id, @NonNull String number, Long client_id) {
        this.id = id;
        this.isNew = id == null;
        this.number = number;
        this.client_id = client_id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public void setClient_id(Long client_id) {
        this.client_id = client_id;
    }

    public Long getClient_id() {
        return client_id;
    }

    public void setNew(boolean aNew) {
        isNew = aNew;
    }

    @Override
    public boolean isNew() {
        return isNew;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Phone phone = (Phone) o;
        return Objects.equals(id, phone.id) && Objects.equals(number, phone.number);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, number);
    }

    @Override
    public String toString() {
        return "Phone{" +
                "id=" + id +
                ", number='" + number + '\'' +
                '}';
    }
}

package org.example.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "customers")
@DiscriminatorValue("CUSTOMER")
public class Customer extends User {

    private String address;

    public Customer() {
        super();
    }

    public Customer(String name, String email, String password, String phone, String address) {
        super(name, email, password, phone, Role.ROLE_CUSTOMER);
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}

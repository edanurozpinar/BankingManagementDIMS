package com.sau.bankingmangpro2.entity;

import com.sau.bankingmangpro2.dto.CustomerDto;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Data
@Entity
@Table(name="customer",schema="dims_db")
public class Customer {
    @Id
    @Column(name = "account_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long customerId;

    @Column(name = "customer_name")
    private String customerName;

    @Column(name = "address")
    private String address;

    @Column(name = "city")
    private String city;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    private List<Loan> loans;

    public Customer(Long customerId, String customerName, String address, String city) {
        this.customerId = customerId;
        this.customerName = customerName;
        this.address = address;
        this.city = city;
    }

    public Customer() {

    }

    public CustomerDto viewAsCustomerDto() {
        return new CustomerDto(customerId, customerName, address,city);
    }

}

package com.sau.bankingmanagementp1.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor

@RequiredArgsConstructor
public class Customer {

    private Integer id;
    private String name;
    private String address;
    private String city;

}

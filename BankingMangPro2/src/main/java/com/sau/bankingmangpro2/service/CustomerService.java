package com.sau.bankingmangpro2.service;

import com.sau.bankingmangpro2.dto.CustomerDto;

import java.util.List;

public interface CustomerService {
    public List<CustomerDto> getAlCustomers();
    public CustomerDto getCustomerById(Long id);
    public CustomerDto createCustomer(CustomerDto customerDto);
    public CustomerDto updateCustomer(Long id, CustomerDto customerDto);
    public void deleteCustomer(Long id);
}

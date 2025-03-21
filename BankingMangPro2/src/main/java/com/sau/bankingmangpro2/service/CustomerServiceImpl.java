package com.sau.bankingmangpro2.service;

import com.sau.bankingmangpro2.dto.CustomerDto;
import com.sau.bankingmangpro2.entity.Customer;
import com.sau.bankingmangpro2.exception.ErrorMessages;
import com.sau.bankingmangpro2.exception.ResourceAlreadyExistsException;
import com.sau.bankingmangpro2.exception.ResourceNotFoundException;
import com.sau.bankingmangpro2.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;

    @Override
    public List<CustomerDto> getAlCustomers() {
        return customerRepository.findAll().stream().map(Customer::viewAsCustomerDto).toList();
    }

    @Override
    public CustomerDto getCustomerById(Long id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorMessages.ERROR_CUSTOMER_NOT_FOUND + ": " + id)).viewAsCustomerDto();
    }

    @Override
    public CustomerDto createCustomer(CustomerDto customerDto) {
        Customer customer = new Customer(customerDto.getCustomerId()
                , customerDto.getCustomerName()
                , customerDto.getAddress()
                ,customerDto.getCity());
        return customerRepository.save(customer).viewAsCustomerDto();
    }

    @Override
    public CustomerDto updateCustomer(Long id, CustomerDto customerDto) {
        Customer existingCustomer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorMessages.ERROR_CUSTOMER_NOT_FOUND + ": " + id));

        if(customerDto.getCustomerName() != null) {
            existingCustomer.setCustomerName(customerDto.getCustomerName());
        }

        if(customerDto.getAddress() != null) {
            existingCustomer.setAddress(customerDto.getAddress());
        }

        if(customerDto.getCity() != null) {
            existingCustomer.setCity(customerDto.getCity());
        }

        Customer updatedCustomer = customerRepository.save(existingCustomer);
        return customerRepository.save(updatedCustomer).viewAsCustomerDto();
    }

    @Override
    public void deleteCustomer(Long id) {
        customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorMessages.ERROR_CUSTOMER_NOT_FOUND + ": " + id));
        customerRepository.deleteById(id);
    }

}

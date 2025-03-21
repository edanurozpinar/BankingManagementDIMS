package com.sau.bankingmanagementp1.controller;

import com.sau.bankingmanagementp1.model.Customer;
import com.sau.bankingmanagementp1.service.CustomerService;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class CustomerController {

    private final CustomerService customerService = new CustomerService();
    @FXML
    private TextField customerIdField;

    @FXML
    private TextField nameField;

    @FXML
    private TextField addressField;

    @FXML
    private TextField cityField;

    @FXML
    private void handleFetch() {
        int id = Integer.parseInt(customerIdField.getText());

        Customer customer = customerService.fetchCustomerById(id);
            nameField.setText(customer.getName());
            addressField.setText(customer.getAddress());
            cityField.setText(customer.getCity());
    }

    @FXML
    private void handleSave() {
        Customer customer = new Customer();
        customer.setName(nameField.getText());
        customer.setAddress(addressField.getText());
        customer.setCity(cityField.getText());

        customerService.saveCustomer(customer);

    }

    @FXML
    private void handleUpdate() {
        Customer customer = new Customer(
                Integer.parseInt(
                        customerIdField.getText()),
                nameField.getText(),
                addressField.getText(),
                cityField.getText()
        );

        customerService.updateCustomer(customer);

    }

    @FXML
    private void handleDelete() {
        int id = Integer.parseInt(customerIdField.getText());

        customerService.deleteCustomer(id);

    }

}

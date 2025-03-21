package com.sau.bankingmanagementp1.service;

import com.sau.bankingmanagementp1.DAO.CustomerDAO;
import com.sau.bankingmanagementp1.controller.CustomerController;
import com.sau.bankingmanagementp1.model.Customer;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class CustomerService {

    public void saveCustomer(Customer customer) {
        if ( customer.getName().isEmpty() ||
                customer.getAddress().isEmpty() || customer.getCity().isEmpty()) {
            showAlert("WARN", "Name, address ,city fields must be filled!", AlertType.ERROR);
            System.out.println("Name, address ,city fields must be filled!");
            return;
        }

        CustomerDAO.saveCustomer(customer);
        showAlert("Success", "Customer has been successfully saved!", AlertType.INFORMATION);
        System.out.println("Customer has been successfully saved");
    }

    public void updateCustomer(Customer customer) {
        if (customer.getId() == 0) {
            showAlert("WARN", "ID is required for update!", AlertType.ERROR);
            System.out.println("ID is required for update!");
            return;
        }
        CustomerDAO.updateCustomer(customer);
        showAlert("Success", "Customer has been successfully updated!", AlertType.INFORMATION);
        System.out.println("Customer updated successfully!");
    }

    public void deleteCustomer(int id) {
        if (id == 0) {
            showAlert("WARN", "ID is required for update!", AlertType.ERROR);
            System.out.println("ID is required for deletion!");
            return;
        }
        CustomerDAO.deleteCustomer(id);
        showAlert("Success", "Customer has been successfully deleted!", AlertType.INFORMATION);
        System.out.println("Customer deleted successfully!");


    }

    public Customer fetchCustomerById(int id) {

        if (id == 0) {
            showAlert("Warning", "ID is required to fetch a customer!", AlertType.WARNING);
            System.out.println("ID is required to fetch customer!");
            return null;
        }

        Customer customer = CustomerDAO.getCustomerById(id);

        if (customer == null) {
            showAlert("Error", "Customer with ID " + id + " not found!", AlertType.ERROR);
            return null;
        }

        showAlert("Success", "Customer with ID " + id + " found successfully!", AlertType.INFORMATION);
        return customer;

    }
    private void showAlert(String title, String message, AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


}

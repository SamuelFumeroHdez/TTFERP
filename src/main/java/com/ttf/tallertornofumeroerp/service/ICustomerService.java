package com.ttf.tallertornofumeroerp.service;

import com.ttf.tallertornofumeroerp.model.Customer;

import java.util.List;

public interface ICustomerService {

    List<Customer> retrieveAllCustomers();
    Customer retrieveCustomer(String nif);
    Customer createCustomer(Customer customer);
    Customer updateCustomer(String nif, Customer customer);
    void deleteCustomer(String nif);
}

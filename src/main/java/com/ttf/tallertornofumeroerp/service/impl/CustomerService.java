package com.ttf.tallertornofumeroerp.service.impl;

import com.ttf.tallertornofumeroerp.exception.customer.CustomerNotFoundException;
import com.ttf.tallertornofumeroerp.model.Customer;
import com.ttf.tallertornofumeroerp.model.Email;
import com.ttf.tallertornofumeroerp.model.InvoiceLine;
import com.ttf.tallertornofumeroerp.repository.ICustomerRepository;
import com.ttf.tallertornofumeroerp.repository.IEmailRespository;
import com.ttf.tallertornofumeroerp.service.ICustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class CustomerService implements ICustomerService {

    @Autowired
    ICustomerRepository customerRepository;

    @Autowired
    IEmailRespository emailRespository;

    @Override
    public List<Customer> retrieveAllCustomers() {
        return customerRepository.findAll();
    }

    @Override
    public Customer retrieveCustomer(String nif) {
        return customerRepository.findById(nif).orElse(null);
    }

    @Override
    public Customer createCustomer(Customer customer) {
        Customer customerDB = retrieveCustomer(customer.getNif());
        if(customerDB != null){
            return customerDB;
        }

        List<Email> emails = customer.getEmails();
        for(Email email : emails){
            email.setCustomer(customer);
            emailRespository.save(email);
        }

        customer.setCreationDate(new Date());
        return customerRepository.save(customer);
    }

    @Override
    public Customer updateCustomer(String nif, Customer customer) {
        Customer customerDB = retrieveCustomer(nif);
        if(customerDB == null){
            return null;
        }
        customerDB.setNif(customer.getNif());
        customerDB.setCustomerName(customer.getCustomerName());
        customerDB.setLegalName(customer.getLegalName());
        customerDB.setAddress(customer.getAddress());
        customerDB.setUpdateDate(new Date());
        return customerRepository.save(customerDB);
    }

    @Override
    public void deleteCustomer(String nif) {
        Customer customerDB = retrieveCustomer(nif);
        if(customerDB == null){
            throw new CustomerNotFoundException("Can't delete an non-existent customer");
        }
        customerRepository.delete(customerDB);
    }

}

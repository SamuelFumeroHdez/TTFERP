package com.ttf.tallertornofumeroerp.controller;

import com.ttf.tallertornofumeroerp.exception.customer.CustomerNotFoundException;
import com.ttf.tallertornofumeroerp.model.Customer;
import com.ttf.tallertornofumeroerp.service.impl.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/customers")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @GetMapping(
            value = "/",
            produces = "application/json")
    public ResponseEntity<List<Customer>> retrieveAllCustomers(){
        List<Customer> customerList = customerService.retrieveAllCustomers();
        return customerList.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(customerList);
    }

    @GetMapping(
            value = "/{nif}",
            produces = "application/json")
    public ResponseEntity<Customer> retrieveCustomer(@PathVariable("nif") String nif){
        Customer customerFound = customerService.retrieveCustomer(nif);
        return customerFound == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(customerFound);
    }

    @PostMapping(
            value = "/create",
            produces = "application/json")
    public ResponseEntity<Customer> createCustomer(@RequestBody Customer customer){
        Customer customerCreated = customerService.createCustomer(customer);
        return ResponseEntity.status(HttpStatus.CREATED).body(customerCreated);
    }

    @PutMapping(value = "/{nif}")
    public ResponseEntity<Customer> updateCustomer(@PathVariable("nif") String nif, @RequestBody Customer customer){
        Customer customerDB = customerService.updateCustomer(nif, customer);
        return customerDB == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(customer);
    }

    @DeleteMapping(value = "/{nif}")
    public void deleteCustomer(@PathVariable("nif") String nif) throws CustomerNotFoundException {
        customerService.deleteCustomer(nif);
    }
}

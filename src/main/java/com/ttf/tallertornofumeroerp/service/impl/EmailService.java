package com.ttf.tallertornofumeroerp.service.impl;

import com.ttf.tallertornofumeroerp.exception.customer.CustomerNotFoundException;
import com.ttf.tallertornofumeroerp.exception.email.EmailNotFoundException;
import com.ttf.tallertornofumeroerp.exception.email.ExistingEmailException;
import com.ttf.tallertornofumeroerp.model.Customer;
import com.ttf.tallertornofumeroerp.model.Email;
import com.ttf.tallertornofumeroerp.repository.IEmailRespository;
import com.ttf.tallertornofumeroerp.service.IEmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmailService implements IEmailService {

    @Autowired
    private IEmailRespository emailRespository;

    @Autowired
    private CustomerService customerService;

    @Override
    public List<Email> retrieveAllEmails() {
        return emailRespository.findAll();
    }

    @Override
    public Email retrieveEmail(String emailAddress) {
        Email email = emailRespository.findById(emailAddress).orElse(null);
        if(email == null){
            throw new EmailNotFoundException("This email doesn't exists");
        }
        return email;
    }

    @Override
    public Email createEmail(Email email) {
        Email emailDB = emailRespository.findById(email.getEmail()).orElse(null);
        if(emailDB != null){
            throw new ExistingEmailException("This email already exists");
        }

        Customer customer = customerService.retrieveCustomer(email.getCustomerNif());
        if (customer == null){
            throw new CustomerNotFoundException("The user doesn't exists");
        }

        email.setCustomer(customer);
        return emailRespository.save(email);
    }

    @Override
    public Email updateEmail(String emailAddress, Email email) {
        Email emailDB = emailRespository.findById(email.getEmail()).orElse(null);
        if(emailDB != null){
            throw new ExistingEmailException("This email already exists");
        }

        Customer customer = customerService.retrieveCustomer(email.getCustomerNif());
        if (customer == null){
            throw new CustomerNotFoundException("The user doesn't exists");
        }

        email.setCustomer(customer);
        email.setEmail(email.getEmail());

        return emailRespository.save(email);
    }

    @Override
    public void deleteEmail(String emailAddress) {
        Email emailDB = emailRespository.findById(emailAddress).orElse(null);
        if(emailDB == null){
            throw new EmailNotFoundException("Can't delete a non-existent email");
        }
        emailRespository.delete(emailDB);
    }
}

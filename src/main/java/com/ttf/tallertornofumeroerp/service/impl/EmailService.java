package com.ttf.tallertornofumeroerp.service.impl;

import com.ttf.tallertornofumeroerp.exception.customer.CustomerNotFoundException;
import com.ttf.tallertornofumeroerp.exception.email.EmailNotFoundException;
import com.ttf.tallertornofumeroerp.exception.email.ExistingEmailException;
import com.ttf.tallertornofumeroerp.exception.invoice.InvoiceNotFoundException;
import com.ttf.tallertornofumeroerp.model.Customer;
import com.ttf.tallertornofumeroerp.model.Email;
import com.ttf.tallertornofumeroerp.model.Invoice;
import com.ttf.tallertornofumeroerp.repository.IEmailRespository;
import com.ttf.tallertornofumeroerp.service.IEmailService;
import com.ttf.tallertornofumeroerp.service.IInvoiceService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmailService implements IEmailService {

    @Autowired
    private IEmailRespository emailRespository;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private IInvoiceService invoiceService;

    @Autowired
    private JavaMailSender javaMailSender;


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
        Email emailDB = emailRespository.findById(emailAddress).orElse(null);
        if(emailDB == null){
            throw new ExistingEmailException("This email doesn't exists");
        }
        Email newEmail = emailRespository.findById(email.getEmail()).orElse(null);
        if(newEmail != null){
            throw new ExistingEmailException("This new email already exists");
        }

        Customer customer = customerService.retrieveCustomer(email.getCustomerNif());
        if (customer == null){
            throw new CustomerNotFoundException("The user doesn't exists");
        }

        email.setCustomer(customer);
        email.setEmail(email.getEmail());
        deleteEmail(emailAddress);

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

    @Override
    public void sendEmail(String invoiceNumber) {
        Invoice invoice = invoiceService.retrieveInvoice(invoiceNumber);
        if(invoice == null){
            throw new InvoiceNotFoundException("The invoice doesn't exists");
        }

        Customer customer = customerService.retrieveCustomer(invoice.getCustomerNif());
        if(customer == null){
            throw new CustomerNotFoundException("The customer doesn't exists");
        }



        String customerName = customer.getCustomerName();
        String customerEmail = customer.getEmails().get(0).getEmail();

        MimeMessage message = javaMailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(customerEmail);
            helper.setSubject("Invoice " + invoiceNumber);
            helper.setText("<strong>Invoice " + invoiceNumber + " details" + "</strong>" +
                    "<p>Customer: " + customerName + "</p>", true); // Configura el contenido como HTML

            javaMailSender.send(message);
        } catch (MessagingException e) {
            // Manejar excepciones
        }

        /*SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(customerEmail);
        message.setSubject("Invoice " + invoiceNumber);
        message.setText("<strong>Invoice " + invoiceNumber + " details" + "</strong>" +
                "<p>Customer: " + customerName + "</p>");

        javaMailSender.send(message);*/
    }
}

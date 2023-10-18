package com.ttf.tallertornofumeroerp.service;

import com.ttf.tallertornofumeroerp.model.Email;

import java.util.List;

public interface IEmailService {
    List<Email> retrieveAllEmails();
    Email retrieveEmail(String emailAddress);
    Email createEmail(Email email);
    Email updateEmail(String emailAddress, Email email);
    void deleteEmail(String emailAddress);
    //void sendEmail(String invoiceNumber);
}

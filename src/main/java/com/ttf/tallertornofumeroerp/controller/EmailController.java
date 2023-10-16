package com.ttf.tallertornofumeroerp.controller;

import com.ttf.tallertornofumeroerp.exception.email.EmailNotFoundException;
import com.ttf.tallertornofumeroerp.model.Email;
import com.ttf.tallertornofumeroerp.service.impl.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/emails")
public class EmailController {

    @Autowired
    private EmailService emailService;

    @GetMapping(
            value = "/",
            produces = "application/json")
    public ResponseEntity<List<Email>> retrieveAllEmails(){
        List<Email> emailList = emailService.retrieveAllEmails();
        return emailList.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(emailList);
    }

    @GetMapping(
            value = "/{emailAddress}",
            produces = "application/json")
    public ResponseEntity<Email> retrieveEmail(@PathVariable("emailAddress") String emailAddress){
        Email emailFound = emailService.retrieveEmail(emailAddress);
        return emailFound == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(emailFound);
    }

    @PostMapping(
            value = "/create",
            produces = "application/json")
    public ResponseEntity<Email> createEmail(@RequestBody Email email){
        Email emailCreated = emailService.createEmail(email);
        return ResponseEntity.status(HttpStatus.CREATED).body(emailCreated);
    }

    @PutMapping(value = "/{emailAddress}")
    public ResponseEntity<Email> updateEmail(@PathVariable("emailAddress") String emailAddress, @RequestBody Email email){
        Email emailDB = emailService.updateEmail(emailAddress, email);
        return emailDB == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(email);
    }

    @DeleteMapping(value = "/{emailAddress}")
    public void deleteEmail(@PathVariable("emailAddress") String emailAddress) throws EmailNotFoundException {
        emailService.deleteEmail(emailAddress);
    }

    @PostMapping(
            value = "/sendEmail/{invoiceNumber}"
    )
    public void sendEmail(@PathVariable("invoiceNumber") String invoiceNumber){
        emailService.sendEmail(invoiceNumber);
    }
}

package com.ttf.tallertornofumeroerp.utils;


import com.ttf.tallertornofumeroerp.model.Email;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.EmailAttachment;
import org.springframework.beans.factory.annotation.Value;
import org.apache.commons.mail.HtmlEmail;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class EmailSender {

    @Value("${spring.mail.host}")
    private String smtpHost;

    @Value("${spring.mail.port}")
    private int smtpPort;

    @Value("${spring.mail.username}")
    private String smtpUsername;

    @Value("${spring.mail.password}")
    private String smtpPassword;

    public void sendEmail(List<Email> emails, String subject, String htmlContent, String attachmentPath) {
        try {


            // Crea un objeto de correo electrónico HTML
            HtmlEmail emailObject = new HtmlEmail();

            // Configura el servidor SMTP y las credenciales desde las propiedades de Spring Boot
            emailObject.setHostName(smtpHost);
            emailObject.setSmtpPort(smtpPort);
            emailObject.setAuthenticator(new DefaultAuthenticator(smtpUsername, smtpPassword));
            emailObject.setStartTLSEnabled(true);

            emailObject.setFrom(smtpUsername);

            for(Email email : emails){
                emailObject.addTo(email.getEmail(), email.getEmail().substring(0,email.getEmail().indexOf('@')));
            }

            emailObject.setSubject(subject);
            emailObject.setHtmlMsg(htmlContent);
            emailObject.setCharset("UTF-8");

            if(attachmentPath != null){
                EmailAttachment attachment = new EmailAttachment();
                attachment.setPath(attachmentPath);
                attachment.setDisposition(EmailAttachment.ATTACHMENT);
                emailObject.attach(attachment);
            }

            emailObject.send();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

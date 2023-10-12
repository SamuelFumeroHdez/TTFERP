package com.ttf.tallertornofumeroerp.exception;


import com.ttf.tallertornofumeroerp.error.Error;
import com.ttf.tallertornofumeroerp.error.ErrorResponse;
import com.ttf.tallertornofumeroerp.exception.customer.CustomerNotFoundException;
import com.ttf.tallertornofumeroerp.exception.customer.ExistingCustomerException;
import com.ttf.tallertornofumeroerp.exception.email.EmailNotFoundException;
import com.ttf.tallertornofumeroerp.exception.email.ExistingEmailException;
import com.ttf.tallertornofumeroerp.exception.invoice.ExistingInvoiceException;
import com.ttf.tallertornofumeroerp.exception.invoice.InvoiceNotFoundException;
import com.ttf.tallertornofumeroerp.exception.invoiceline.ExistingInvoiceLineException;
import com.ttf.tallertornofumeroerp.exception.invoiceline.InvoiceLineNotFoundException;
import com.ttf.tallertornofumeroerp.exception.user.ExistingUserException;
import com.ttf.tallertornofumeroerp.exception.user.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Arrays;

@ControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handlerUserNotFoundException(UserNotFoundException ex){
        ErrorResponse errorResponse = ErrorResponse.builder()
                .errors(Arrays.asList(Error.builder()
                        .code("USER_NOT_FOUND")
                        .message(ex.getMessage())
                        .build()))
                .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(ExistingUserException.class)
    public ResponseEntity<ErrorResponse> handlerExistingRestaurantException(ExistingUserException ex){
        ErrorResponse errorResponse = ErrorResponse.builder()
                .errors(Arrays.asList(Error.builder()
                        .code("EXISTING_USER")
                        .message(ex.getMessage())
                        .build()))
                .build();
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }

    @ExceptionHandler(CustomerNotFoundException.class)
    public ResponseEntity<ErrorResponse> handlerCustomerNotFoundException(CustomerNotFoundException ex){
        ErrorResponse errorResponse = ErrorResponse.builder()
                .errors(Arrays.asList(Error.builder()
                        .code("CUSTOMER_NOT_FOUND")
                        .message(ex.getMessage())
                        .build()))
                .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(ExistingCustomerException.class)
    public ResponseEntity<ErrorResponse> handlerExistingCustomerException(ExistingCustomerException ex){
        ErrorResponse errorResponse = ErrorResponse.builder()
                .errors(Arrays.asList(Error.builder()
                        .code("EXISTING_CUSTOMER")
                        .message(ex.getMessage())
                        .build()))
                .build();
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }

    @ExceptionHandler(InvoiceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handlerInvoiceNotFoundException(InvoiceNotFoundException ex){
        ErrorResponse errorResponse = ErrorResponse.builder()
                .errors(Arrays.asList(Error.builder()
                        .code("INVOICE_NOT_FOUND")
                        .message(ex.getMessage())
                        .build()))
                .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(ExistingInvoiceException.class)
    public ResponseEntity<ErrorResponse> handlerExistingInvoiceException(ExistingInvoiceException ex){
        ErrorResponse errorResponse = ErrorResponse.builder()
                .errors(Arrays.asList(Error.builder()
                        .code("EXISTING_INVOICE")
                        .message(ex.getMessage())
                        .build()))
                .build();
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }

    @ExceptionHandler(InvoiceLineNotFoundException.class)
    public ResponseEntity<ErrorResponse> handlerInvoiceLineNotFoundException(InvoiceLineNotFoundException ex){
        ErrorResponse errorResponse = ErrorResponse.builder()
                .errors(Arrays.asList(Error.builder()
                        .code("INVOICE_LINE_NOT_FOUND")
                        .message(ex.getMessage())
                        .build()))
                .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(ExistingInvoiceLineException.class)
    public ResponseEntity<ErrorResponse> handlerExistingInvoiceLineException(ExistingInvoiceLineException ex){
        ErrorResponse errorResponse = ErrorResponse.builder()
                .errors(Arrays.asList(Error.builder()
                        .code("EXISTING_INVOICE_LINE")
                        .message(ex.getMessage())
                        .build()))
                .build();
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }

    @ExceptionHandler(EmailNotFoundException.class)
    public ResponseEntity<ErrorResponse> handlerEmailNotFoundException(EmailNotFoundException ex){
        ErrorResponse errorResponse = ErrorResponse.builder()
                .errors(Arrays.asList(Error.builder()
                        .code("EMAIL_NOT_FOUND")
                        .message(ex.getMessage())
                        .build()))
                .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(ExistingEmailException.class)
    public ResponseEntity<ErrorResponse> handlerExistingEmailException(ExistingEmailException ex){
        ErrorResponse errorResponse = ErrorResponse.builder()
                .errors(Arrays.asList(Error.builder()
                        .code("EXISTING_EMAIL")
                        .message(ex.getMessage())
                        .build()))
                .build();
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }



}

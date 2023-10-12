package com.ttf.tallertornofumeroerp.exception.customer;

public class ExistingCustomerException extends RuntimeException{
    public ExistingCustomerException(String message){
        super(message);
    }
}

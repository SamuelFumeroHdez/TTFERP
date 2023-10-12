package com.ttf.tallertornofumeroerp.exception.invoice;

public class ExistingInvoiceException extends RuntimeException{
    public ExistingInvoiceException(String message){
        super(message);
    }
}

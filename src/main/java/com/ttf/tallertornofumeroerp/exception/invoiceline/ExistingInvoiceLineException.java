package com.ttf.tallertornofumeroerp.exception.invoiceline;

public class ExistingInvoiceLineException extends RuntimeException{
    public ExistingInvoiceLineException(String message){
        super(message);
    }
}

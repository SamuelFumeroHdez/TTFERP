package com.ttf.tallertornofumeroerp.exception.email;

public class ExistingEmailException extends RuntimeException{
    public ExistingEmailException(String message){
        super(message);
    }
}

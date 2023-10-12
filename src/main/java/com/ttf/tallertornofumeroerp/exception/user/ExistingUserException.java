package com.ttf.tallertornofumeroerp.exception.user;

public class ExistingUserException extends RuntimeException{
    public ExistingUserException(String message){
        super(message);
    }
}

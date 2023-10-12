package com.ttf.tallertornofumeroerp.exception.user;

public class UserNotFoundException extends RuntimeException{

    public UserNotFoundException(String message){
        super(message);
    }
}

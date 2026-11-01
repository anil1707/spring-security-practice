package com.example.spring_security_demo.exceptions;

public class InvalidCredentialException extends RuntimeException {
    public InvalidCredentialException(String message){
        super(message);
    }
}

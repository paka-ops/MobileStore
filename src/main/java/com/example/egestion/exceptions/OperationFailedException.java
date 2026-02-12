package com.example.egestion.exceptions;

public class OperationFailedException extends RuntimeException{
    private String message;
    public OperationFailedException(String message){
        super(message);
    }
}

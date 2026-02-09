package com.example.egestion.exceptions;

public class CreationFailedException extends RuntimeException{
    public CreationFailedException(String message){
        super(message);
    }
}

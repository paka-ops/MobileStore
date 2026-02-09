package com.example.egestion.exceptions;

public class UpdateFailedException extends RuntimeException{
    public UpdateFailedException(String message){
        super(message);
    }
}

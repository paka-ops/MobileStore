package com.example.egestion.exceptions;

public class ElementNotFoundException extends RuntimeException{
    public ElementNotFoundException(String message){
        super(message);
    }
}

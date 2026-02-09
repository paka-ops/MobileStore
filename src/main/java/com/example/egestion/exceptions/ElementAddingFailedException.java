package com.example.egestion.exceptions;

public class ElementAddingFailedException extends RuntimeException{
    private String message;

    public ElementAddingFailedException(String message) {
        super(message);
    }
}

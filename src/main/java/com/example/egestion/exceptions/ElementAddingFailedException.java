package com.example.egestion.exceptions;

public class ElementAddingFailedException extends Exception{
    private String message;

    public ElementAddingFailedException(String message) {
        super(message);
    }
}

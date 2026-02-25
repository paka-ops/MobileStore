package com.example.egestion.configuration;

import com.example.egestion.exceptions.*;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Component
@RestControllerAdvice
public class ResponseBuilder {
    public Map<String,Object> responseBody(String status, String message){
        Map<String,Object> response = new HashMap<>();
        response.put("status",status);
        response.put("message",message);
        return response;
    }
    public Map<String,Object> responseBody(String status,String message,Object object){
        Map<String,Object> response = new HashMap<>();
        response.put("status",status);
        response.put("message",message);
        if(object != null)response.put(Arrays.stream(object.getClass().toString().split("\\.")).toList().getLast() ,object);
        response.put("object",null);
        return response;
    }
    public Map<String,Object> responseBody(String status, String message, List<Object> objects){
        Map<String,Object> response = new HashMap<>();
        response.put("status",status);
        response.put("message",message);
        response.put(objects.getClass().toString() + "s",objects);
        return response;
    }
    @ExceptionHandler(NotAuthorizedException.class)
    public ResponseEntity NotAuthorizedResponse(NotAuthorizedException exception){
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(responseBody("UNAUTHORIZED" , exception.getMessage()));
    }
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity AccessDeniedResponse(AccessDeniedException exception){
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(responseBody("ACCESS DENIED" , exception.getMessage()));
    }
    @ExceptionHandler(NotAuthenticatedException.class)
    public ResponseEntity NotAuthenticatedResponse(NotAuthenticatedException exception){
        return ResponseEntity.status(HttpStatus.PRECONDITION_REQUIRED)
                .body(responseBody("PRECONDITION REQUIRED" , exception.getMessage()));
    }
    @ExceptionHandler(CreationFailedException.class)
    public  ResponseEntity CreationFailedResponse(CreationFailedException exception){
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(responseBody("INTERNAL_SERVER_ERROR" , exception.getMessage()));

    }
    @ExceptionHandler(UpdateFailedException.class)
    public ResponseEntity UpdateFailedResponse(UpdateFailedException exception){
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(responseBody("INTERNAL SERVER ERROR", exception.getMessage()));
    }
    @ExceptionHandler(ElementNotFoundException.class)
    public ResponseEntity ElementNotFoundResponse(ElementNotFoundException exception){
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(responseBody("NOT_FOUND" , exception.getMessage()));
    }
    @ExceptionHandler(ElementAddingFailedException.class)
    public ResponseEntity elementAddingFailedResponse(ElementAddingFailedException e){
        return  ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(responseBody("INTERNAL SERVER ERROR","ADDING ELEMENT FAILED"));
    }
}

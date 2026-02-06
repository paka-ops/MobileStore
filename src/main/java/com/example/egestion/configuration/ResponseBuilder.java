package com.example.egestion.configuration;

import com.example.egestion.exceptions.*;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Component
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
        if(object != null)response.put(object.getClass().toString() ,object);
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
    public ResponseEntity NotAuthorizedResponse(NotAuthorizedException exception){
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(responseBody("UNAUTHORIZED" , exception.getMessage()));
    }
    public ResponseEntity AccessDeniedResponse(AccessDeniedException exception){
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(responseBody("ACCESS DENIED" , exception.getMessage()));
    }

    public ResponseEntity NotAuthenticatedResponse(NotAuthenticatedException exception){
        return ResponseEntity.status(HttpStatus.PRECONDITION_REQUIRED)
                .body(responseBody("PRECONDITION REQUIRED" , exception.getMessage()));
    }
    public  ResponseEntity CreationFailedResponse(CreationFailedException exception){
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(responseBody("INTERNAL_SERVER_ERROR" , exception.getMessage()));

    }
    public ResponseEntity UpdateFailedResponse(UpdateFailedException exception){
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(responseBody("INTERNAL SERVER ERROR", exception.getMessage()));
    }
    public ResponseEntity ElementNotFoundResponse(ElementNotFoundException exception){
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(responseBody("NOT_FOUND" , exception.getMessage()));
    }
    public ResponseEntity elementAddingFailedResponse(ElementAddingFailedException e){
        return  ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(responseBody("INTERNAL SERVER ERROR","ADDING ELEMENT FAILED"));
    }
}

package com.example.egestion.configuration;

import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Configuration
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
        response.put(object.getClass().toString(),object);
        return response;
    }
    public Map<String,Object> responseBody(String status, String message, List<Object> object){
        Map<String,Object> response = new HashMap<>();
        response.put("status",status);
        response.put("message",message);
        response.put(object.getClass().toString()+"s",object);
        return response;
    }
}

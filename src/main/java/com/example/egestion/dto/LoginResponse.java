package com.example.egestion.dto;

import com.example.egestion.security.JwtService;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
@Component
public class LoginResponse {
    private final JwtService jwtService;
    private String status;
    private String userType;
    private String message;
    private String username;
    private String jwt;

    public LoginResponse(JwtService jwtService) {
        this.jwtService = jwtService;
    }


    public Map<String,String> response(String status, String userType, String message, String username){
        Map<String ,String> response = new HashMap<>();
        response.put("status",status);
        response.put("message",message);
        response.put("username",username);
        response.put("userType",userType);
        response.put("jwtToken",jwtService.jwtGenerator(username));
        return response;
    }
}

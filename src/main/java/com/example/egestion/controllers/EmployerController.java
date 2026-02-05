package com.example.egestion.controllers;

import com.example.egestion.configuration.ResponseBuilder;
import com.example.egestion.exceptions.CreationFailedException;
import com.example.egestion.exceptions.ElementNotFoundException;
import com.example.egestion.exceptions.NotAuthenticatedException;
import com.example.egestion.models.Employer;
import com.example.egestion.services.implementations.EmployerService;
import jakarta.ws.rs.NotAuthorizedException;
import jakarta.xml.ws.http.HTTPBinding;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Controller
@RequestMapping(path = "/employers",produces = "application/json")
public class EmployerController {
    private final EmployerService emService;
    private final ResponseBuilder responseBuilder;
    public EmployerController(EmployerService emService, ResponseBuilder responseBuilder) {
        this.emService = emService;
        this.responseBuilder = responseBuilder;
    }
    @GetMapping(produces = "application/json")
    @PreAuthorize("Admin")
    public ResponseEntity getAll( ){
           List<Employer> employers = emService.getAll();
           return ResponseEntity.status(HttpStatus.OK)
                   .body(responseBuilder.responseBody("OK","Found",employers));
    }
    @GetMapping(path = "/{id}")
    public ResponseEntity getOne(@PathVariable UUID id){
        try {
            Employer employer = emService.getOne(id);
            return ResponseEntity.ok(employer);
        }catch (ElementNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
    @PatchMapping(value = "/{id}")
    public ResponseEntity update(@PathVariable UUID id,@RequestBody Employer employer){
        try{
            Employer emp = emService.update(employer, id);
            Map<String,Object> body = responseBuilder.responseBody("CREATED","update successfully",emp);
            return ResponseEntity.status(HttpStatus.CREATED).body(body);
        } catch (ElementNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(responseBuilder.responseBody("NOT_FOUND",e.getMessage()));
        }
    }
    @PostMapping()
    public ResponseEntity create(@RequestBody Employer employer){
        try{
            Employer emp = emService.create(employer);
            return  ResponseEntity.status(HttpStatus.CREATED)
                    .body(responseBuilder.responseBody("CREATED","Creation successfully",emp));
        } catch (CreationFailedException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(responseBuilder.responseBody("INTERNAL_SERVER_ERROR",e.getMessage()));
        }
    }








}

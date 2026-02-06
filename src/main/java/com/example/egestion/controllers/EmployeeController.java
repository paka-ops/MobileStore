package com.example.egestion.controllers;

import com.example.egestion.configuration.ResponseBuilder;
import com.example.egestion.exceptions.*;
import com.example.egestion.models.Employee;
import com.example.egestion.models.Employer;
import com.example.egestion.services.implementations.EmployeeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

@Controller
@RequestMapping("/api/v1/employee")
public class EmployeeController {
    private final EmployeeService emService;
    private final ResponseBuilder response;

    public EmployeeController(EmployeeService emService, ResponseBuilder response) {
        this.emService = emService;
        this.response = response;
    }

    @GetMapping(produces = "application/json")
    public ResponseEntity getAll(){
        try{
            List<Employee> employees = emService.getAll();
            return ResponseEntity.status(HttpStatus.OK)
                    .body(response.responseBody("OK","Found",employees));
        } catch (AccessDeniedException e) {
            return response.AccessDeniedResponse(e);
        } catch (NotAuthenticatedException e) {
            return response.NotAuthenticatedResponse(e);
        } catch (NotAuthorizedException e) {
            return response.NotAuthorizedResponse(e);
        }

    }
    @GetMapping("/{id}")
    public ResponseEntity getOne(@PathVariable UUID id){
        try{
            Employee employee = emService.getOne(id);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(response.responseBody("OK","FOUND",employee));
        } catch (NotAuthenticatedException e) {
            return response.NotAuthenticatedResponse(e);
        } catch (NotAuthorizedException e) {
            return response.NotAuthorizedResponse(e);
        }catch (ElementNotFoundException e){
            return response.ElementNotFoundResponse(e);
        }
    }
    @PostMapping(consumes = "application/json")
    public ResponseEntity create(@RequestBody Employee employee){
        try{
            Employee employee1 = emService.create(employee);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(response.responseBody("CREATED","CREATION SUCCESSFULLY",employee1));
        } catch (NotAuthenticatedException e) {
            return response.NotAuthenticatedResponse(e);
        } catch (NotAuthorizedException e) {
            return response.NotAuthorizedResponse(e);
        }catch (AccessDeniedException e) {
            return response.AccessDeniedResponse(e);
        } catch (CreationFailedException e) {
            return response.CreationFailedResponse(e);
        }
    }
    @PatchMapping("/{employeeId}")
    public  ResponseEntity update(@RequestBody Employee employee,@PathVariable UUID employeeId){
        try{
            Employee employee1 = emService.update(employee,employeeId);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(response.responseBody("CREATED","UPDATE SUCCESSFULLY",employee1));
        } catch (AccessDeniedException e) {
            return response.AccessDeniedResponse(e);
        } catch (NotAuthenticatedException e) {
            return response.NotAuthenticatedResponse(e);
        } catch (ElementNotFoundException e) {
            return response.ElementNotFoundResponse(e);
        } catch (NotAuthorizedException e) {
            return response.NotAuthorizedResponse(e);
        } catch (UpdateFailedException e) {
            return response.UpdateFailedResponse(e);
        }
    }
    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable UUID id){
        try{
            emService.delete(id);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(response.responseBody("OK","DELETED SUCCESSFULLY"));
        } catch (AccessDeniedException e) {
            return response.AccessDeniedResponse(e);
        } catch (NotAuthenticatedException e) {
            return response.NotAuthenticatedResponse(e);
        } catch (ElementNotFoundException e) {
            return response.ElementNotFoundResponse(e);
        } catch (NotAuthorizedException e) {
            return response.NotAuthorizedResponse(e);
        }
    }

}

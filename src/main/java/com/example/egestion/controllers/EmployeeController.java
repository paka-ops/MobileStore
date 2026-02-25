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
        List<Employee> employees = emService.getAll();
        return ResponseEntity.status(HttpStatus.OK)
                .body(response.responseBody("OK","Found",employees));
    }
    @GetMapping("/{id}")
    public ResponseEntity getOne(@PathVariable UUID id){
        Employee employee = emService.getOne(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(response.responseBody("OK","FOUND",employee));
    }
    @PostMapping(consumes = "application/json",params = "storeId")
    public ResponseEntity create(@RequestBody Employee employee,@RequestParam UUID storeId){
        Employee employee1 = emService.create(employee,storeId);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(response.responseBody("CREATED","CREATION SUCCESSFULLY",employee1));
    }
    @PatchMapping("/{employeeId}")
    public  ResponseEntity update(@RequestBody Employee employee,@PathVariable UUID employeeId){
        Employee employee1 = emService.update(employee,employeeId);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(response.responseBody("CREATED","UPDATE SUCCESSFULLY",employee1));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable UUID id){
        emService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .body(response.responseBody("OK","DELETED SUCCESSFULLY"));
    }
    @GetMapping(params = "storeId")
    public ResponseEntity getAllByStore(@RequestParam UUID storeId){
        List<Employee> employees = emService.getAllByStoreId(storeId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(response.responseBody("OK","Found",employees));
    }

}

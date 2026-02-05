package com.example.egestion.controllers;

import com.example.egestion.models.Employee;
import com.example.egestion.services.implementations.EmployeeService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/api/v1/employee")
public class EmployeeController {
    private final EmployeeService emService;

    public EmployeeController(EmployeeService emService) {
        this.emService = emService;
    }


}

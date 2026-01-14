package com.example.egestion.services.interfaces;

import com.example.egestion.exceptions.CreationFailedException;
import com.example.egestion.exceptions.ElementNotFoundException;
import com.example.egestion.exceptions.UpdateFailedException;
import com.example.egestion.models.Category;
import com.example.egestion.models.Employee;
import com.example.egestion.models.Employer;
import com.example.egestion.models.Product;

import java.util.List;
import java.util.UUID;

public interface IEmployer {
    Employer create(Employer employer) throws CreationFailedException;
    Employer update(Employer employer, UUID id) throws UpdateFailedException;
    Employee getEmployee(UUID id);
    List<Employee> employees();
    Employee getOne(UUID id) throws ElementNotFoundException;



}


package com.example.egestion.services.interfaces;

import com.example.egestion.exceptions.*;
import com.example.egestion.models.Category;
import com.example.egestion.models.Employee;
import com.example.egestion.models.Employer;
import com.example.egestion.models.Product;

import java.util.List;
import java.util.UUID;

public interface IEmployer {
    Employer create(Employer employer) throws CreationFailedException, NotAuthenticatedException, AccessDeniedException,NotAuthorizedException;
    Employer update(Employer employer, UUID id) throws UpdateFailedException, NotAuthenticatedException, AccessDeniedException,NotAuthorizedException;
    Employee getEmployee(UUID id) throws NotAuthenticatedException,NotAuthorizedException;
    List<Employee> employees() throws  NotAuthenticatedException, AccessDeniedException,NotAuthorizedException ;



}


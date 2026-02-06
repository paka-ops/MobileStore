package com.example.egestion.services.interfaces;

import com.example.egestion.exceptions.*;
import com.example.egestion.models.Employee;
import com.example.egestion.models.Order;

import java.util.List;
import java.util.UUID;

public interface IEmployee {
    Employee create(Employee employee) throws CreationFailedException, NotAuthenticatedException, AccessDeniedException,NotAuthorizedException, NotAuthenticatedException;
    Employee update(Employee employee, UUID id) throws UpdateFailedException, NotAuthenticatedException, AccessDeniedException,NotAuthorizedException,ElementNotFoundException;
    void delete(UUID id) throws  NotAuthenticatedException, AccessDeniedException,NotAuthorizedException,ElementNotFoundException;
    List<Employee> getAll() throws  NotAuthenticatedException, AccessDeniedException,NotAuthorizedException;
    Employee getOne(UUID id) throws ElementNotFoundException, NotAuthenticatedException, NotAuthorizedException;

}

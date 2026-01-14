package com.example.egestion.services.interfaces;

import com.example.egestion.exceptions.CreationFailedException;
import com.example.egestion.exceptions.ElementNotFoundException;
import com.example.egestion.exceptions.UpdateFailedException;
import com.example.egestion.models.Employee;
import com.example.egestion.models.Order;

import java.util.List;
import java.util.UUID;

public interface IEmployee {
    Employee create(Employee employee) throws CreationFailedException;
    Employee update(Employee employee, UUID id) throws UpdateFailedException;
    void delete(UUID id);
    List<Employee> getAll();
    Employee getOne(UUID id) throws ElementNotFoundException ;

}

package com.example.egestion.services.implementations;

import com.example.egestion.exceptions.*;
import com.example.egestion.models.Employee;
import com.example.egestion.repositories.EmployeeRepository;
import com.example.egestion.repositories.EmployeeRepository;
import com.example.egestion.services.interfaces.IEmployee;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
@Service
public class EmployeeService implements IEmployee  {
    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository){
        this.employeeRepository = employeeRepository;

    }

    @Override
    public Employee create(Employee employee) throws CreationFailedException,
            NotAuthenticatedException, AccessDeniedException, NotAuthorizedException {
        
            try {
                Employee e = this.employeeRepository.save(employee);
                return e;
            }catch (Exception e){
                throw  new CreationFailedException("creation error");
            }
    }

    @Override
    public Employee update(Employee employee, UUID id) throws UpdateFailedException, NotAuthenticatedException,
            AccessDeniedException, NotAuthorizedException,ElementNotFoundException {
        
        if(!employeeRepository.existsById(id)) throw new ElementNotFoundException("user not found ");
        try{
            employee.setId(id);
            Employee e = employeeRepository.save(employee);
            return e;
        }catch(Exception e){
            throw new UpdateFailedException("update error ");
        }

    }

    @Override
    public void delete(UUID id) throws NotAuthenticatedException, AccessDeniedException,
            NotAuthorizedException ,ElementNotFoundException{
        
        if(!employeeRepository.existsById(id)) throw new ElementNotFoundException("user not found ");
        employeeRepository.deleteById(id);
    }

    @Override
    public List<Employee> getAll() throws NotAuthenticatedException, AccessDeniedException, NotAuthorizedException {
        return  employeeRepository.findAll();
    }

    @Override
    public Employee getOne(UUID id) throws ElementNotFoundException {
        Optional<Employee> employee =  employeeRepository.findById(id);
        return employee.orElseThrow(()->new ElementNotFoundException("user not found"));
    }


}

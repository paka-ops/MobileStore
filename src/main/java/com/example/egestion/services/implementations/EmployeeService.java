package com.example.egestion.services.implementations;

import com.example.egestion.exceptions.*;
import com.example.egestion.models.Employee;
import com.example.egestion.models.Employer;
import com.example.egestion.models.Person;
import com.example.egestion.repositories.EmployeeRepository;
import com.example.egestion.repositories.PersonRepository;
import com.example.egestion.security.SecCheck;
import com.example.egestion.services.interfaces.IEmployee;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
@Service
public class EmployeeService implements IEmployee  {
    private final EmployeeRepository employeeRepository;
    private  final SecCheck  secCheck;
    private final PersonRepository personRepository;
    private final PasswordEncoder encoder;

    public EmployeeService(EmployeeRepository employeeRepository, SecCheck secCheck, PersonRepository personRepository, PasswordEncoder encoder){
        this.employeeRepository = employeeRepository;
        this.secCheck = secCheck;
        this.personRepository = personRepository;
        this.encoder = encoder;
    }

    @Override
    public Employee create(Employee employee) throws CreationFailedException,
            NotAuthenticatedException, AccessDeniedException, NotAuthorizedException {
            this.secCheck.hasRole("EMPLOYER");
            Authentication authEmp = SecurityContextHolder.getContext().getAuthentication();
            String username = authEmp.getName();
            Employer employer = (Employer) personRepository.findByUsername(username);
            try {
                employee.setEmployer(employer);
                Employee e = this.employeeRepository.save(employee);
                return e;
            }catch (Exception e){
                throw  new CreationFailedException("creation error");
            }
    }

    @Override
    public Employee update(Employee employee, UUID id) throws UpdateFailedException, NotAuthenticatedException,
            AccessDeniedException, NotAuthorizedException,ElementNotFoundException {
        this.secCheck.hasRole("EMPLOYER");
        Optional<Employee> employee1 = employeeRepository.findById(id);
        if(!employee1.isPresent()) throw new ElementNotFoundException("user not found ");
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        Employer employer =(Employer) personRepository.findByUsername(username);
        this.secCheck.isEmployeeOfEmployer(employer, employee1.get());
        try{
            employee.setId(id);
            if(employee.getUsername() != null){
                employee1.get().setUsername(employee.getUsername());
            }
            if(employee.getFirstname() != null){
                employee1.get().setFirstname(employee.getFirstname());
            }
            if(employee.getSecondeName() != null){
                employee1.get().setSecondeName(employee.getSecondeName());
            }
            if(employee.getPassword() !=  null){
                employee1.get().setPassword(encoder.encode(employee.getPassword()));
            }
            if(employee.getPhone() != null){
                employee1.get().setPhone(employee.getPhone());
            }
            if(employee.getStore() != null){
                employee1.get().setStore(employee.getStore());
            }
            if(employee.getStatus() != null){
                employee1.get().setStatus(employee.getStatus());
            }
            if(employee.getPost() != null){
                employee1.get().setPost(employee.getPost());
            }
            if(employee.getStatus() != null){
                employee1.get().setStatus(employee.getStatus());
            }
            Employee e = employeeRepository.save(employee1.get());
            return e;
        }catch(Exception e){
            throw new UpdateFailedException("update error ");
        }

    }

    @Override
    public void delete(UUID id) throws NotAuthenticatedException, AccessDeniedException,
            NotAuthorizedException ,ElementNotFoundException{
        this.secCheck.hasRole("EMPLOYER");
        Optional<Employee> employee = employeeRepository.findById(id);
        if(!employee.isPresent()) throw new ElementNotFoundException("user not found ");
        Employer employer = (Employer) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        this.secCheck.isEmployeeOfEmployer(employer,employee.get());
        employeeRepository.deleteById(id);
    }

    @Override
    public List<Employee> getAll() throws NotAuthenticatedException,
            AccessDeniedException, NotAuthorizedException {
        this.secCheck.hasRole("EMPLOYER");
        Employer employer = (Employer) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return employeeRepository.findEmployeesByEmployer(employer);
    }

    @Override
    public Employee getOne(UUID id) throws ElementNotFoundException,
            NotAuthenticatedException, NotAuthorizedException {
        this.secCheck.hasRole("EMPLOYER");
        Optional<Employee> employee =  employeeRepository.findById(id);
        return employee.orElseThrow(()->new ElementNotFoundException("user not found"));
    }



}

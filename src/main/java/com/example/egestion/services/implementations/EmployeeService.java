package com.example.egestion.services.implementations;

import com.example.egestion.exceptions.*;
import com.example.egestion.models.Employee;
import com.example.egestion.models.Employer;
import com.example.egestion.models.Store;
import com.example.egestion.repositories.EmployeeRepository;
import com.example.egestion.repositories.PersonRepository;
import com.example.egestion.repositories.StoreRepository;
import com.example.egestion.security.SecurityValidator;
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
    private  final SecurityValidator secCheck;
    private final PersonRepository personRepository;
    private final PasswordEncoder encoder;
    private final StoreRepository storeRepository;


    public EmployeeService(EmployeeRepository employeeRepository, SecurityValidator secCheck, PersonRepository personRepository, PasswordEncoder encoder, StoreRepository storeRepository){
        this.employeeRepository = employeeRepository;
        this.secCheck = secCheck;
        this.personRepository = personRepository;
        this.encoder = encoder;
        this.storeRepository = storeRepository;
    }

    @Override
    @PreAuthorize("hasRole('EMPLOYER')")
    public Employee create(Employee employee,UUID storeId) throws CreationFailedException,
            NotAuthenticatedException, AccessDeniedException, NotAuthorizedException {
            secCheck.validateStoreAccess(storeId);
            Store store = storeRepository.findById(storeId)
                    .orElseThrow(()->new ElementNotFoundException("Store not found"));
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            Employer employer = secCheck.findUserFromAuthentication(auth, Employer.class);
            try {
                employee.setEmployer(employer);
                employee.setStore(store);
                String password = encoder.encode(employee.getPassword());
                employee.setPassword(password);
                Employee e = this.employeeRepository.save(employee);
                return e;
            }catch (Exception e){
                throw  new CreationFailedException("creation error");
            }
    }

    @Override
    @PreAuthorize("hasRole('EMPLOYER')")
    public Employee update(Employee employee, UUID id) throws UpdateFailedException, NotAuthenticatedException,
            AccessDeniedException, NotAuthorizedException,ElementNotFoundException {
        Optional<Employee> employee1 = employeeRepository.findById(id);
        if(!employee1.isPresent()) throw new ElementNotFoundException("user not found ");
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        try{
            employee.setId(id);
            if(employee.getUsername() != null){
                employee1.get().setUsername(employee.getUsername());
            }
            if(employee.getFirstname() != null){
                employee1.get().setFirstname(employee.getFirstname());
            }
            if(employee.getLastname() != null){
                employee1.get().setLastname(employee.getLastname());
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
        this.secCheck.isEmployeeOfEmployer(employer.getId(),employee.get().getId());
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

    @Override
    @PreAuthorize("hasRole('EMPLOYER')")
    public List<Employee> getAllByStoreId(UUID storeId) {
        secCheck.validateStoreAccess(storeId);
        return employeeRepository.findAllByStoreId(storeId);
    }


}

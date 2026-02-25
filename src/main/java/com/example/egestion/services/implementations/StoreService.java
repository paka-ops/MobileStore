package com.example.egestion.services.implementations;

import com.example.egestion.exceptions.*;
import com.example.egestion.models.Employee;
import com.example.egestion.models.Employer;
import com.example.egestion.models.Store;
import com.example.egestion.repositories.EmployeeRepository;
import com.example.egestion.repositories.PersonRepository;
import com.example.egestion.repositories.StoreRepository;
import com.example.egestion.security.SecurityValidator;
import com.example.egestion.services.interfaces.IStore;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
@Service
public class StoreService implements IStore { private final StoreRepository storeRepository;
    private final SecurityValidator securityCheck;
    private final PersonRepository personRepository;
    private final EmployeeRepository employeeRepository;

    public StoreService(StoreRepository storeRepository, SecurityValidator securityCheck, PersonRepository personRepository, EmployeeRepository employeeRepository) {
        this.storeRepository = storeRepository;
        this.securityCheck = securityCheck;
        this.personRepository = personRepository;
        this.employeeRepository = employeeRepository;
    }


    @Override
    @PreAuthorize("hasRole('EMPLOYER')")
    public List<Store> getAll() throws AccessDeniedException, NotAuthenticatedException, NotAuthorizedException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        Employer employer = (Employer) personRepository.findByUsername(username);
        return storeRepository.findStoresByEmployerId(employer.getId());
    }

    @Override
    @PreAuthorize("hasRole('EMPLOYER')")
    public Store getOne(UUID id) throws ElementNotFoundException,
            NotAuthorizedException, AccessDeniedException, NotAuthenticatedException {
        securityCheck.validateStoreAccess(id);
        return storeRepository.getReferenceById(id);
    }

    @Override
    @PreAuthorize("hasRole('EMPLOYER')")
    public Store add(Store store) throws CreationFailedException,
            NotAuthenticatedException, NotAuthorizedException {
        Authentication auth = securityCheck.getAuthentication();
        Employer  employer = securityCheck.findUserFromAuthentication(auth, Employer.class);
        store.setEmployer(employer);
        return storeRepository.save(store);

    }

    @Override
    @PreAuthorize("hasRole('EMPLOYER')")
    public Store update(Store store, UUID id) throws UpdateFailedException, AccessDeniedException,
            NotAuthenticatedException, ElementNotFoundException, NotAuthorizedException {
        securityCheck.validateStoreAccess(id);
        Store existing = storeRepository.getReferenceById(id);
        if (store.getName() != null) {
            existing.setName(store.getName());
        }
        if (store.getLocation() != null) {
            existing.setLocation(store.getLocation());
        }
        if (store.getEmployees() != null) {
            existing.setEmployees(store.getEmployees());
        }
        if (store.getEmployer() != null) {
            existing.setEmployer(store.getEmployer());
        }
        if (store.getCategories() != null) {
            existing.setCategories(store.getCategories());
        }
        existing.setId(id);
        return storeRepository.save(existing);

    }

    @Override
    @PreAuthorize("hasRole('EMPLOYER')")
    public void delete(UUID id) throws ElementNotFoundException, AccessDeniedException,
            NotAuthenticatedException, NotAuthorizedException {
        securityCheck.validateStoreAccess(id);
        storeRepository.deleteById(id);
    }

    @Override
    @PreAuthorize("hasRole('EMPLOYER')")
    public Store addEmployee(UUID storeId,UUID employeeId) throws ElementNotFoundException, AccessDeniedException, NotAuthenticatedException, NotAuthorizedException, ElementAddingFailedException {
        securityCheck.validateStoreAccess(storeId);
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(()-> new ElementNotFoundException("Employee not found "));
       Authentication auth = securityCheck.getAuthentication();
        Employer employer = securityCheck.findUserFromAuthentication(auth, Employer.class);
        Store store = storeRepository.getReferenceById(storeId);
        if(securityCheck.isEmployeeOfEmployer(employer.getId(),employeeId)){
            List<Employee> employees = store.getEmployees();
            employee.setStore(store);
            employees.add(employee);
            store.setEmployees(employees);
            return storeRepository.save(store);

        }
        throw new ElementAddingFailedException("Employee adding failed");
    }



}

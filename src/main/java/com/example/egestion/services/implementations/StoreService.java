package com.example.egestion.services.implementations;

import com.example.egestion.exceptions.*;
import com.example.egestion.models.Employee;
import com.example.egestion.models.Employer;
import com.example.egestion.models.Store;
import com.example.egestion.repositories.EmployeeRepository;
import com.example.egestion.repositories.PersonRepository;
import com.example.egestion.repositories.StoreRepository;
import com.example.egestion.security.SecCheck;
import com.example.egestion.services.interfaces.IStore;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
@Service
public class StoreService implements IStore { private final StoreRepository storeRepository;
    private final SecCheck securityCheck;
    private final PersonRepository personRepository;
    private final EmployeeRepository employeeRepository;

    public StoreService(StoreRepository storeRepository, SecCheck securityCheck, PersonRepository personRepository,EmployeeRepository employeeRepository) {
        this.storeRepository = storeRepository;
        this.securityCheck = securityCheck;
        this.personRepository = personRepository;
        this.employeeRepository = employeeRepository;
    }


    @Override
    public List<Store> getAll() throws AccessDeniedException, NotAuthenticatedException, NotAuthorizedException {
        securityCheck.hasRole("EMPLOYER");
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        Employer employer = (Employer) personRepository.findByUsername(username);
        return storeRepository.findStoresByEmployer(employer);
    }

    @Override
    public Store getOne(UUID id) throws ElementNotFoundException,
            NotAuthorizedException, AccessDeniedException, NotAuthenticatedException {
        securityCheck.hasRole("EMPLOYER");
        Optional<Store> store = storeRepository.findById(id);
        if(!store.isPresent()) throw new ElementNotFoundException("element not found ");
        boolean isMemberOfStore = this.securityCheck.isMemberOfStore(store.get());
        if(!isMemberOfStore) throw new NotAuthorizedException("not authorized");
        return storeRepository.getReferenceById(id);
    }

    @Override
    public Store add(Store store) throws CreationFailedException,
            NotAuthenticatedException, NotAuthorizedException {
        this.securityCheck.hasRole("EMPLOYER");
        try{
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            Employer employer = securityCheck.findUserFromAuthentication(auth, Employer.class);
            store.setEmployer(employer);
            Store st =  storeRepository.save(store);
            return st;
        }catch(Exception e){
            throw new CreationFailedException("error during creation");
        }

    }

    @Override
    public Store update(Store store, UUID id) throws UpdateFailedException, AccessDeniedException,
            NotAuthenticatedException, ElementNotFoundException, NotAuthorizedException {
        this.securityCheck.hasRole("EMPLOYER");
        Optional<Store> st = storeRepository.findById(id);
        if(!st.isPresent()) throw new ElementNotFoundException("element not found ");
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        Employer emp = this.securityCheck.findUserFromAuthentication(auth, Employer.class);
        boolean isMemberOfStore = this.securityCheck.isOwnerOfStore(emp,st.get());
        if(!isMemberOfStore) throw new NotAuthorizedException("not authorized");
        Store existing = st.get();
        if (store.getName() != null) {
            existing.setName(store.getName());
        }
        if (store.getLocalisation() != null) {
            existing.setLocalisation(store.getLocalisation());
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
    public void delete(UUID id) throws ElementNotFoundException, AccessDeniedException,
            NotAuthenticatedException, NotAuthorizedException {
        this.securityCheck.hasRole("EMPLOYER");
        Optional<Store> store = storeRepository.findById(id);
        if(!store.isPresent()) throw new ElementNotFoundException("element not found ");
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Employer em = securityCheck.findUserFromAuthentication(auth, Employer.class);
        securityCheck.isOwnerOfStore(em, store.get());
        storeRepository.deleteById(id);
    }

    @Override
    public Store addEmployee(UUID storeId,UUID employeeId) throws ElementNotFoundException, AccessDeniedException, NotAuthenticatedException, NotAuthorizedException, ElementAddingFailedException {
        securityCheck.hasRole("EMPLOYER");
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Employer employer = securityCheck.findUserFromAuthentication(auth, Employer.class);
        Optional<Employee> employee = employeeRepository.findById(employeeId);
        if(employee.isEmpty()) throw new ElementNotFoundException("Employee not found");
        securityCheck.isEmployeeOfEmployer(employer,employee.get());
        Optional<Store> store = storeRepository.findById(storeId);
        if(store.isEmpty()) throw new ElementNotFoundException("Store not found");
        securityCheck.isOwnerOfStore(employer,store.get());
        List<Employee> employees = store.get().getEmployees();
        employees.add(employee.get());
        store.get().setEmployees(employees);
        employee.get().setStore(store.get());
        employeeRepository.save(employee.get());
        return store.get();
    }


}

package com.example.egestion.security;

import com.example.egestion.exceptions.AccessDeniedException;
import com.example.egestion.exceptions.NotAuthenticatedException;
import com.example.egestion.models.Employee;
import com.example.egestion.models.Employer;
import com.example.egestion.models.Person;
import com.example.egestion.models.Store;
import com.example.egestion.repositories.EmployeeRepository;
import com.example.egestion.repositories.EmployerRepository;
import com.example.egestion.repositories.StoreRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
/**
 * This class contains different methode for checking security rules;
 *
 */
public class SecurityCheck {
    private final StoreRepository storeRepository;
    private final EmployerRepository employerRepository;
    private final EmployeeRepository employeeRepository;

    public SecurityCheck(StoreRepository storeRepository, EmployerRepository employerRepository, EmployeeRepository employeeRepository) {
        this.storeRepository = storeRepository;
        this.employerRepository = employerRepository;
        this.employeeRepository = employeeRepository;
    }

    /**
     * @param role
     * @return true if the user has role
     * @throws NotAuthenticatedException
     * @throws NotAuthenticatedException
     * @throws AccessDeniedException
     */
    public boolean hasRole(String role) throws NotAuthenticatedException, NotAuthenticatedException, AccessDeniedException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            throw new NotAuthenticatedException("user not authenticated");
        }
        boolean authHasRole = auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals(role));
        if (authHasRole) {
            return true;
        } else {
            throw new AccessDeniedException("access denied");
        }
    }

    public boolean isElementOfStore(Store store) throws NotAuthenticatedException,AccessDeniedException{
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(auth == null) throw new AccessDeniedException("access denied");
        if(!auth.isAuthenticated()) throw new NotAuthenticatedException("not authenticated");
        Object  person =  auth.getPrincipal();
        if(person instanceof Employer employer){
            String username = employer.getUsername();
            Employer em = employerRepository.findByUsername(username);
            List<Store> stores = em.getStores();
            return stores.stream().anyMatch(st -> st.equals(store));
        }else if (person instanceof Employee employee){
            String username = employee.getUsername();
            Employee em = employeeRepository.findByUsername(username);
            Store st = em.getStore();
            if(st == null ) return false;
            if(st.equals(store) ) return true;
            return false;
        }
        return false;

    }
}

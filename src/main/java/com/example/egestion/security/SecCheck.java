package com.example.egestion.security;

import com.example.egestion.exceptions.NotAuthenticatedException;
import com.example.egestion.exceptions.NotAuthorizedException;
import com.example.egestion.models.Employee;
import com.example.egestion.models.Employer;
import com.example.egestion.models.Person;
import com.example.egestion.models.Store;
import com.example.egestion.repositories.EmployeeRepository;
import com.example.egestion.repositories.PersonRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SecCheck {
    private final PersonRepository perRepo;
    private final EmployeeRepository epRepository;
    private final PersonRepository personRepository;

    public SecCheck(PersonRepository perRepo, EmployeeRepository epRepository, PersonRepository personRepository) {
        this.perRepo = perRepo;
        this.epRepository = epRepository;
        this.personRepository = personRepository;
    }

    public boolean isMemberOfStore(Store store) throws NotAuthenticatedException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(auth == null || !auth.isAuthenticated()) throw new NotAuthenticatedException("user not authenticated");
        String username  = auth.getName();
        Person person = perRepo.findByUsername(username);
        if(person instanceof Employer){
            if(!((Employer) person).getStores().contains(store))
                throw new NotAuthenticatedException("user  not the owner of the store");
        }else{
            if(!((Employee) person).getStore().equals(store))
                throw new NotAuthenticatedException("user not an employee for this store");
        }
        return true;
    }

    public boolean hasRole(String role) throws NotAuthenticatedException, NotAuthorizedException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(auth == null|| !auth.isAuthenticated()) throw new NotAuthenticatedException("user not authenticated");
        if(!auth.getAuthorities().stream().
                anyMatch(a->a.getAuthority()
                        .contains(role)))
            throw new NotAuthorizedException("user has not role");
        return true;
    }
    public boolean isEmployeeOfEmployer(Employer employer,Employee employee){
        List<Employee> employees = epRepository.findEmployeesByEmployer(employer);
        if(!employees.contains(employee)) return false;
        return true;
    }
    public boolean isOwnerOfStore(Employer employer,Store store){
        if(!store.getEmployer().equals(employer))return false;
        return true;
    }
    public boolean isEmployeeOfStore(Employee employee,Store store){
        if(!store.getEmployees().contains(employee)) return false;
        return true;
    }
    public <T extends  Person> T findUserFromAuthentication(Authentication auth, Class<T> clasz){
        String username = auth.getName();
        return clasz.cast(personRepository.findByUsername(username));
    }
    public Authentication getAuthentication(){
        return SecurityContextHolder.getContext().getAuthentication();
    }
}


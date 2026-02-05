package com.example.egestion.security;

import com.example.egestion.exceptions.NotAuthenticatedException;
import com.example.egestion.exceptions.NotAuthorizedException;
import com.example.egestion.models.Employee;
import com.example.egestion.models.Employer;
import com.example.egestion.models.Person;
import com.example.egestion.models.Store;
import com.example.egestion.repositories.PersonRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class SecCheck {
    private final PersonRepository perRepo;

    public SecCheck(PersonRepository perRepo) {
        this.perRepo = perRepo;
    }

    public boolean isMemberOfStore(Store store) throws NotAuthenticatedException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(auth == null) throw new NotAuthenticatedException("user not authenticated");
        Person authPerson = (Person) auth.getPrincipal();
        String username  = authPerson.getUsername();
        Person person = perRepo.findByUsername(username);
        if(person instanceof Employer){
            if(!((Employer) person).getStores().contains(store)) return false;
        }else{
            if(!((Employee) person).getStore().equals(store)) return false;
        }
        return true;
    }

    public boolean hasRole(String role) throws NotAuthenticatedException, NotAuthorizedException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(auth == null) throw new NotAuthenticatedException("user not authenticated");
        if(!auth.getAuthorities().stream().
                anyMatch(a->a.getAuthority()
                        .equals(role))) throw new NotAuthorizedException("user has not role");
        return true;
    }
}


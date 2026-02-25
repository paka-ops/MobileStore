package com.example.egestion.security;

import com.example.egestion.exceptions.AccessDeniedException;
import com.example.egestion.exceptions.ElementNotFoundException;
import com.example.egestion.exceptions.NotAuthenticatedException;
import com.example.egestion.exceptions.NotAuthorizedException;
import com.example.egestion.models.*;
import com.example.egestion.repositories.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class SecurityValidator {
    private final PersonRepository personRepository;
    private final ProductRepository productRepository;
    private final StoreRepository storeRepository;
    private final CategoryRepository categoryRepository;
    private final EmployerRepository employerRepository;
    private final EmployeeRepository employeeRepository;

    public SecurityValidator( PersonRepository personRepository, ProductRepository productRepository, StoreRepository storeRepository, CategoryRepository categoryRepository, EmployerRepository employerRepository, EmployeeRepository employeeRepository) {

        this.personRepository = personRepository;
        this.productRepository = productRepository;
        this.storeRepository = storeRepository;
        this.categoryRepository = categoryRepository;
        this.employerRepository = employerRepository;
        this.employeeRepository = employeeRepository;
    }

    public boolean hasRole(String role) throws NotAuthenticatedException, NotAuthorizedException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(auth == null|| !auth.isAuthenticated()) throw new NotAuthenticatedException("user not authenticated");
        if(!auth.getAuthorities().stream().anyMatch(a->a.getAuthority()
                .contains(role)))
           return false;
        return true;
    }
    public boolean isEmployeeOfEmployer(UUID employerId,UUID employeeId){
        boolean exist = employeeRepository.existsByIdAndEmployerId(employeeId,employerId);
        if(!exist) return false;
        return true;
    }
    public <T extends  Person> T findUserFromAuthentication(Authentication auth, Class<T> clasz){
        String username = auth.getName();
        return clasz.cast(personRepository.findByUsername(username));
    }
    public Authentication getAuthentication() throws NotAuthenticatedException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(auth == null || !auth.isAuthenticated())throw new NotAuthenticatedException("Login required");
        return  auth;
    }
    public void validateStoreAccess(UUID storeId) throws ElementNotFoundException, AccessDeniedException, NotAuthenticatedException, NotAuthorizedException {
        Authentication auth = getAuthentication();
        Optional<Store> store = storeRepository.findById(storeId);
        if(store.isEmpty()) throw new ElementNotFoundException("Store not found ");
        if(hasRole("EMPLOYER")) {
            Employer employer = findUserFromAuthentication(auth, Employer.class);
            if (!store.get().getEmployer().equals(employer))
                throw new AccessDeniedException("You are not the owner of the store");
        }else if(hasRole("EMPLOYEE")){
            Employee employee = findUserFromAuthentication(auth,Employee.class);
            boolean exists = employeeRepository.existsByIdAndStoreId(employee.getId(),store.get().getId());
            if(!exists) throw new AccessDeniedException("You are not an employee of the store");
        }else throw new AccessDeniedException("You are not a member of a store");
    }
    public void validateProductAccess(UUID productId) throws ElementNotFoundException, NotAuthenticatedException, NotAuthorizedException, AccessDeniedException {
        Product product = productRepository.findById(productId)
                .orElseThrow(()->new ElementNotFoundException("Product not Found"));
        Category category = product.getCategory();
        validateCategoryAccess(category.getId());

    }
    public void validateCategoryAccess(UUID categoryId) throws ElementNotFoundException, AccessDeniedException, NotAuthenticatedException, NotAuthorizedException {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(()->new ElementNotFoundException("Category not found Exception"));
        Store store = category.getStore();
        validateStoreAccess(store.getId());
    }




}


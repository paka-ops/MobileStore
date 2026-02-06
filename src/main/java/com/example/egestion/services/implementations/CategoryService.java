package com.example.egestion.services.implementations;

import com.example.egestion.exceptions.*;
import com.example.egestion.models.Category;
import com.example.egestion.models.Employer;
import com.example.egestion.models.Store;
import com.example.egestion.repositories.CategoryRepository;
import com.example.egestion.repositories.StoreRepository;
import com.example.egestion.security.SecCheck;
import com.example.egestion.services.interfaces.ICategory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
@Service
public class CategoryService implements ICategory {
    private final SecCheck secCheck;
    private final CategoryRepository categoryRepository;
    private final StoreService storeService;
    private final StoreRepository storeRepository;
    private boolean isOwnerOfCategory(Category category){
        Store store = category.getStore();
        Authentication auth = secCheck.getAuthentication();
        Employer employer = secCheck.findUserFromAuthentication(auth, Employer.class);
        secCheck.isOwnerOfStore(employer,store);
        return true;
    }

    public CategoryService(CategoryRepository categoryRepository, SecCheck secCheck, StoreService storeService, StoreRepository storeRepository){
        this.secCheck = secCheck;
        this.categoryRepository = categoryRepository;
        this.storeService = storeService;
        this.storeRepository = storeRepository;
    }
    @Override
    public Category add(Category category,UUID storeId) throws CreationFailedException, UpdateFailedException, NotAuthenticatedException, AccessDeniedException, NotAuthorizedException, ElementNotFoundException {
        this.secCheck.hasRole("ROLE_EMPLOYER");
        Optional<Store> store = storeRepository.findById(storeId);
        if(store.isEmpty()) throw new ElementNotFoundException("Store not found");
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Employer employer = secCheck.findUserFromAuthentication(auth,Employer.class);
        secCheck.isOwnerOfStore(employer,store.get());
        category.setStore(store.get());
        return categoryRepository.save(category);
    }
    @Override
    public Category update(Category category, UUID id) throws UpdateFailedException, UpdateFailedException, NotAuthenticatedException, AccessDeniedException, NotAuthorizedException, ElementNotFoundException {
        this.secCheck.hasRole("ROLE_EMPLOYER");
        this.isOwnerOfCategory(category);
        Optional<Category> category1 = categoryRepository.findById(category.getId());
        if(category1.isEmpty()) throw new ElementNotFoundException("Category not found");
        if(category.getName() != null){
            category1.get().setName(category.getName());
        }if(category.getProducts() != null){
            category1.get().setProducts(category.getProducts());
        }if(category.getStore() != null){
            category1.get().setStore(category.getStore());
        }
        return categoryRepository.save(category1.get());
    }

    @Override
    public void delete(UUID categoryId) throws ElementNotFoundException, NotAuthenticatedException, AccessDeniedException, NotAuthorizedException {
        this.secCheck.hasRole("ROLE_EMPLOYER");
        Optional<Category> category = categoryRepository.findById(categoryId);
        if(category.isEmpty()) throw new ElementNotFoundException("Category not found ");
        this.isOwnerOfCategory(category.get());
        categoryRepository.deleteById(categoryId);
    }

    @Override
    public List<Category> getAll() throws UpdateFailedException, NotAuthenticatedException, AccessDeniedException, NotAuthorizedException, ElementNotFoundException {
       /* this.secCheck.hasRole("ROLE_EMPLOYER");
        this.isOwnerOfCategory()
        return categoryRepository.findAll();*/
        return new ArrayList<>();
    }

    @Override
    public List<Category> getAllByStore(UUID storeId) throws ElementNotFoundException, AccessDeniedException, NotAuthenticatedException, NotAuthorizedException {
        secCheck.hasRole("EMPLOYER");
        Optional<Store> store   =  storeRepository.findById(storeId);
        if(store.isEmpty()) throw new ElementNotFoundException("Store not found");
        Authentication auth = secCheck.getAuthentication();
        Employer employer = secCheck.findUserFromAuthentication(auth,Employer.class);
        List<Category> categories = store.get().getCategories();
        return categories;
    }

    @Override
    public Category getOne(UUID id) throws ElementNotFoundException, NotAuthenticatedException, NotAuthorizedException {
        secCheck.hasRole("EMPLOYER");
        Category category = categoryRepository.findById(id).orElseThrow(()->new ElementNotFoundException("element not found"));
        this.isOwnerOfCategory(category);
        return category;
    }


}

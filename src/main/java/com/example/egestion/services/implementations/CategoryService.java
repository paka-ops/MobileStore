package com.example.egestion.services.implementations;

import com.example.egestion.exceptions.*;
import com.example.egestion.models.Category;
import com.example.egestion.models.Employer;
import com.example.egestion.models.Store;
import com.example.egestion.repositories.CategoryRepository;
import com.example.egestion.repositories.StoreRepository;
import com.example.egestion.security.SecurityValidator;
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
    private final SecurityValidator secCheck;
    private final CategoryRepository categoryRepository;
    private final StoreService storeService;
    private final StoreRepository storeRepository;


    public CategoryService(CategoryRepository categoryRepository, SecurityValidator secCheck, StoreService storeService, StoreRepository storeRepository){
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
        secCheck.validateStoreAccess(storeId);
        category.setStore(store.get());
        return categoryRepository.save(category);
    }
    @Override
    public Category update(Category category, UUID id) throws UpdateFailedException, UpdateFailedException, NotAuthenticatedException, AccessDeniedException, NotAuthorizedException, ElementNotFoundException {
        this.secCheck.hasRole("ROLE_EMPLOYER");
        secCheck.validateCategoryAccess(id);
        Optional<Category> category1 = categoryRepository.findById(id);
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
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(()-> new ElementNotFoundException("Category not found "));
        secCheck.validateCategoryAccess(category.getId());
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
        if(!(secCheck.hasRole("EMPLOYER") || secCheck.hasRole("EMPLOYEE"))) throw new AccessDeniedException("your not allowed") ;
        secCheck.validateStoreAccess(storeId);
        Optional<Store> store   =  storeRepository.findById(storeId);
        List<Category> categories = store.get().getCategories();
        return categories;
    }

    @Override
    public Category getOne(UUID id) throws ElementNotFoundException, NotAuthenticatedException, NotAuthorizedException, AccessDeniedException {
        secCheck.hasRole("EMPLOYER");
        Category category = categoryRepository.findById(id).orElseThrow(()->new ElementNotFoundException("element not found"));
        secCheck.validateCategoryAccess(category.getId());
        return category;
    }


}

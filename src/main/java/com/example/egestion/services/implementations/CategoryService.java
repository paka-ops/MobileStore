package com.example.egestion.services.implementations;

import com.example.egestion.exceptions.*;
import com.example.egestion.models.Category;
import com.example.egestion.models.Store;
import com.example.egestion.repositories.CategoryRepository;
import com.example.egestion.services.interfaces.ICategory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
@Service
public class CategoryService {
   /* private final SecurityCheck secCheck;
    private final CategoryRepository categoryRepository;
    private final StoreService storeService;
    public CategoryService(CategoryRepository categoryRepository, SecurityCheck secCheck, StoreService storeService){
        this.secCheck = secCheck;
        this.categoryRepository = categoryRepository;
        this.storeService = storeService;
    }
    @Override
    public Category add(Category category) throws CreationFailedException, UpdateFailedException, NotAuthenticatedException, AccessDeniedException, NotAuthorizedException, ElementNotFoundException {
        this.secCheck.hasRole("ROLE_EMPLOYER");
        try {
            Category cat = categoryRepository.save(category);
            return cat;
        }catch(Exception e){
            throw new CreationFailedException("error during creation");
        }
    }
    @Override
    public Category update(Category category, UUID id) throws UpdateFailedException, UpdateFailedException, NotAuthenticatedException, AccessDeniedException, NotAuthorizedException, ElementNotFoundException {
        this.secCheck.hasRole("ROLE_EMPLOYER");
        boolean isExist= categoryRepository.existsById(id);
        if(!isExist)throw new ElementNotFoundException("element not found");
        category.setId(id);
        Category cat = categoryRepository.save(category);
        return cat;
    }

    @Override
    public void delete(UUID categoryId) throws ElementNotFoundException, NotAuthenticatedException, AccessDeniedException {
        this.secCheck.hasRole("ROLE_EMPLOYER");
        boolean isExist = categoryRepository.existsById(categoryId);
        if(!isExist) throw new ElementNotFoundException("element not found Exception");
        categoryRepository.deleteById(categoryId);

    }

    @Override
    public List<Category> getAll() throws UpdateFailedException, NotAuthenticatedException, AccessDeniedException, NotAuthorizedException, ElementNotFoundException {
        this.secCheck.hasRole("ROLE_EMPLOYER");
        return categoryRepository.findAll();
    }

    @Override
    public List<Category> getAllByStore(UUID storeId) throws ElementNotFoundException, AccessDeniedException, NotAuthenticatedException, NotAuthorizedException {
        Store store   =  storeService.getOne(storeId);

        return List.of();
    }

    @Override
    public Category getOne(UUID id) throws ElementNotFoundException {
        Optional<Category> category = categoryRepository.findById(id);
        return category.orElseThrow(()->new ElementNotFoundException("element not found"));
    }

    */
}

package com.example.egestion.services.interfaces;

import com.example.egestion.exceptions.*;
import com.example.egestion.models.Category;

import java.util.List;
import java.util.UUID;

public interface ICategory {
    Category add(Category category,UUID storeId) throws CreationFailedException ,UpdateFailedException, NotAuthenticatedException, AccessDeniedException,NotAuthorizedException,ElementNotFoundException;
    Category update(Category category, UUID id) throws UpdateFailedException,UpdateFailedException, NotAuthenticatedException, AccessDeniedException,NotAuthorizedException,ElementNotFoundException;
    void  delete(UUID categoryId) throws ElementNotFoundException, UpdateFailedException, NotAuthenticatedException, AccessDeniedException,NotAuthorizedException,ElementNotFoundException;
    List<Category> getAll() throws UpdateFailedException, NotAuthenticatedException, AccessDeniedException,NotAuthorizedException,ElementNotFoundException;
    List<Category> getAllByStore(UUID storeId) throws ElementNotFoundException, AccessDeniedException, NotAuthenticatedException, NotAuthorizedException;
    Category getOne(UUID id) throws ElementNotFoundException, NotAuthenticatedException, NotAuthorizedException, AccessDeniedException;
}

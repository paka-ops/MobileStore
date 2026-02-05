package com.example.egestion.services.interfaces;

import com.example.egestion.exceptions.*;
import com.example.egestion.models.Category;
import com.example.egestion.models.Product;

import java.util.List;
import java.util.UUID;

public interface IProduct {
    Product add(Product product) throws CreationFailedException, NotAuthenticatedException, AccessDeniedException,NotAuthorizedException;
    Product update(Product product, UUID id) throws UpdateFailedException, NotAuthenticatedException, AccessDeniedException,NotAuthorizedException, ElementNotFoundException;
    void delete(UUID productId) throws UpdateFailedException, NotAuthenticatedException, AccessDeniedException, NotAuthorizedException, ElementNotFoundException;
    List<Product> getAll() throws AccessDeniedException, NotAuthenticatedException;
    Product getOne(UUID id) throws ElementNotFoundException;
}

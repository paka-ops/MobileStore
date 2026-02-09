package com.example.egestion.services.interfaces;

import com.example.egestion.exceptions.*;
import com.example.egestion.models.Product;

import java.util.List;
import java.util.UUID;

public interface IProduct {
    Product add(Product product,UUID categorieId) throws CreationFailedException, NotAuthenticatedException, AccessDeniedException, NotAuthorizedException, ElementNotFoundException;
    Product update(Product product, UUID id) throws UpdateFailedException, NotAuthenticatedException, AccessDeniedException,NotAuthorizedException, ElementNotFoundException;
    void delete(UUID productId) throws UpdateFailedException, NotAuthenticatedException, AccessDeniedException, NotAuthorizedException, ElementNotFoundException;
    List<Product> getAllByStore(UUID storeId) throws AccessDeniedException, NotAuthenticatedException, NotAuthorizedException, ElementNotFoundException;
    Product getOneByStore(UUID productId,UUID storeId) throws ElementNotFoundException, NotAuthenticatedException, NotAuthorizedException, AccessDeniedException;
    List<Product> getAllByCategory(UUID categoryId) throws AccessDeniedException, NotAuthenticatedException, NotAuthorizedException, ElementNotFoundException;
    Product getOneByCategory(UUID productId,UUID categoryId) throws ElementNotFoundException, NotAuthenticatedException, NotAuthorizedException, AccessDeniedException;
    Product getProductByNameAndByStore(String productName, UUID storeId);
    Product getProductByProductNameAndByCategory(String productName,UUID categoryId);
}

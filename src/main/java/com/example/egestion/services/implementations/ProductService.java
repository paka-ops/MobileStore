package com.example.egestion.services.implementations;

import com.example.egestion.exceptions.*;
import com.example.egestion.models.Category;
import com.example.egestion.models.Product;
import com.example.egestion.models.Store;
import com.example.egestion.repositories.CategoryRepository;
import com.example.egestion.repositories.ProductRepository;

import com.example.egestion.security.SecCheck;
import com.example.egestion.services.interfaces.IProduct;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class ProductService implements IProduct {
    private final ProductRepository productRepository;
    private final SecCheck securityCheck;
    private final CategoryRepository categoryRepository;

    public ProductService(ProductRepository productRepository, SecCheck securityCheck, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.securityCheck = securityCheck;
        this.categoryRepository = categoryRepository;
    }


    @Override
    public Product add(Product product,UUID categoryId) throws CreationFailedException, NotAuthenticatedException, AccessDeniedException, NotAuthorizedException, ElementNotFoundException {
        this.securityCheck.hasRole("ROLE_EMPLOYER");
        Optional<Category> category = categoryRepository.findById(categoryId);
        if(category.isEmpty()) throw new ElementNotFoundException("Categorie not found ");
        try{
            Product pro = productRepository.save(product);
            return pro;
        }catch(Exception e){
            throw new CreationFailedException("error during the creation");
        }
    }

    @Override
    public Product update(Product product, UUID id) throws UpdateFailedException, NotAuthenticatedException, AccessDeniedException, NotAuthorizedException,ElementNotFoundException {
        this.securityCheck.hasRole("ROLE_EMPLOYER");
        boolean isExist  = productRepository.existsById(id);
        if(!isExist) throw new ElementNotFoundException("element not found ");
        product.setId(id);
        Product prod = productRepository.save(product);
        return prod;
    }

    @Override
    public void delete(UUID productId) throws UpdateFailedException, NotAuthenticatedException, AccessDeniedException, NotAuthorizedException, ElementNotFoundException {
        this.securityCheck.hasRole("ROLE_EMPLOYER");
        Optional<Product> product = productRepository.findById(productId);
        if(!product.isPresent()) throw new ElementNotFoundException("element not found: product not found");
        productRepository.deleteById(productId);
    }

    @Override
    public List<Product> getAll() throws AccessDeniedException, NotAuthenticatedException, NotAuthorizedException {
        this.securityCheck.hasRole("ROLE_EMPLOYER");
        return productRepository.findAll();
    }

    @Override
    public Product getOne(UUID id) throws ElementNotFoundException {
        boolean isExist = productRepository.existsById(id);
        if(!isExist) throw new ElementNotFoundException("element not found ");
        return productRepository.findById(id).get();
    }

}

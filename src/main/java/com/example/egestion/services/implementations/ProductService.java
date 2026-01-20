package com.example.egestion.services.implementations;

import com.example.egestion.exceptions.*;
import com.example.egestion.models.Product;
import com.example.egestion.repositories.ProductRepository;
import com.example.egestion.security.SecurityCheck;
import com.example.egestion.services.interfaces.IProduct;

import java.util.List;
import java.util.UUID;

public class ProductService implements IProduct {
    private final ProductRepository productRepository;
    private final SecurityCheck securityCheck;

    public ProductService(ProductRepository productRepository, SecurityCheck securityCheck) {
        this.productRepository = productRepository;
        this.securityCheck = securityCheck;
    }

    @Override
    public Product add(Product product) throws CreationFailedException, NotAuthenticatedException, AccessDeniedException, NotAuthorizedException {
        this.securityCheck.hasRole("ROLE_EMPLOYER");
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
    public void delete(UUID productId) throws UpdateFailedException, NotAuthenticatedException, AccessDeniedException, NotAuthorizedException {
        this.securityCheck.hasRole("ROLE_EMPLOYER");
        productRepository.deleteById(productId);
    }

    @Override
    public List<Product> getAll() throws AccessDeniedException, NotAuthenticatedException {
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

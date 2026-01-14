package com.example.egestion.services.interfaces;

import com.example.egestion.exceptions.CreationFailedException;
import com.example.egestion.exceptions.ElementNotFoundException;
import com.example.egestion.exceptions.UpdateFailedException;
import com.example.egestion.models.Category;
import com.example.egestion.models.Product;

import java.util.List;
import java.util.UUID;

public interface IProduct {
    Product add(Product product, Category category, int Quantity) throws CreationFailedException;
    Product update(Product product, UUID id) throws UpdateFailedException;
    void delete(UUID productId)throws UpdateFailedException;
    List<Product> getAll();
    Product getOne(UUID id) throws ElementNotFoundException;
}

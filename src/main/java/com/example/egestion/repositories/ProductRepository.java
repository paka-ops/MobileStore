package com.example.egestion.repositories;

import com.example.egestion.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID> {
    List<Product> getAllByDeletedFalse();
    List<Product> getAllByDeletedFalseAndCategoryId(UUID categoryId);
}

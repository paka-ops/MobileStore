package com.example.egestion.repositories;

import com.example.egestion.models.Category;
import com.example.egestion.models.Store;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface CategoryRepository extends JpaRepository<Category, UUID> {
  boolean existsAllByIdInAndStoreId(List<UUID> categoriesId,UUID storeId);
}

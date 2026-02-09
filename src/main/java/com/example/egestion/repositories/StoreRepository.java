package com.example.egestion.repositories;

import com.example.egestion.models.Employer;
import com.example.egestion.models.Store;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface StoreRepository extends JpaRepository<Store, UUID> {
    List<Store> findStoresByEmployer(UUID employerId);
    boolean existsEmployeeById(UUID employeeId,UUID storeId);
}

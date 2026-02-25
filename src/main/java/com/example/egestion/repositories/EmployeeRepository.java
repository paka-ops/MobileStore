package com.example.egestion.repositories;

import com.example.egestion.models.Employee;
import com.example.egestion.models.Employer;
import com.example.egestion.models.Store;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface EmployeeRepository extends JpaRepository<Employee, UUID> {

    List<Employee> findEmployeesByEmployer(Employer employer);
    boolean existsByIdAndEmployerId(UUID employeeId,UUID employerId);
    boolean existsByIdAndStoreId(UUID employeeId,UUID storeId);
    List<Employee> findAllByStoreId(UUID storeId);

    UUID store(Store store);
}

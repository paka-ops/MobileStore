package com.example.egestion.repositories;

import com.example.egestion.models.Employer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.UUID;

public interface EmployerRepository extends JpaRepository<Employer, UUID> {
    UserDetails findByUsername(String username);
    boolean existsEmployeeById(UUID employeeId,UUID employerId);
}

package com.example.egestion.repositories;

import com.example.egestion.models.Employer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface EmployerRepository extends JpaRepository<Employer, UUID> {
}

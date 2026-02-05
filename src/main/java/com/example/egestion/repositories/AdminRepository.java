package com.example.egestion.repositories;

import com.example.egestion.models.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AdminRepository extends JpaRepository<Admin, UUID> {
    Admin findByUsername(String username);
}


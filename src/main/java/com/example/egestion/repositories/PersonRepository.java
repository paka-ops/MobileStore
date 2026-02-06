package com.example.egestion.repositories;

import com.example.egestion.models.Employee;
import com.example.egestion.models.Employer;
import com.example.egestion.models.Person;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PersonRepository extends JpaRepository<Person, UUID> {

    Person findByUsername(String username);

}

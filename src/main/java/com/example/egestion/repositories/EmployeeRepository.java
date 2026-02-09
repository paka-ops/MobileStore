package com.example.egestion.repositories;

import com.example.egestion.models.Employee;
import com.example.egestion.models.Employer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface EmployeeRepository extends JpaRepository<Employee, UUID> {

    List<Employee> findEmployeesByEmployer(Employer employer);


}

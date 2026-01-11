package com.example.egestion.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Inheritance;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.lang.annotation.Inherited;
import java.util.ArrayList;
import java.util.List;
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Employer extends Person {
    @OneToMany
    private List<Store> stores = new ArrayList<>();
    @OneToMany(mappedBy = "employer",orphanRemoval = true)
    private List<Employee> employees = new ArrayList<>();
}

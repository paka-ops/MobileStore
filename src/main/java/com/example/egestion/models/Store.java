package com.example.egestion.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Data @NoArgsConstructor @AllArgsConstructor

public class Store {
    @Id @GeneratedValue @UuidGenerator(style = UuidGenerator.Style.TIME)
    private UUID id;
    private String name;
    private String localisation;
    @OneToMany(mappedBy ="store",orphanRemoval = false)
    @JsonIgnore
    List<Employee> employees = new ArrayList<>();
    @ManyToOne(cascade = CascadeType.ALL)
    private Employer employer;
    @OneToMany(mappedBy = "store")
    @JsonIgnore
    private List<Order> order;
    @OneToMany(mappedBy = "store", orphanRemoval = true)
    @JsonIgnore
    private List<Category>categories = new ArrayList<>();
    @PreRemove
    private void deleteStore(){
        if(employees != null){
            for(Employee e : employees){
                e.setStore(null);
            }

        }
    }
}

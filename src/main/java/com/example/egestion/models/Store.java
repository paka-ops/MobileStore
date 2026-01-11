package com.example.egestion.models;

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
    @OneToMany(mappedBy ="store")
    List<Employee> employee = new ArrayList<>();
    @ManyToOne
    private Employer employer;
    @ManyToMany
    @JoinTable(name = "stores_category",joinColumns = @JoinColumn(name="category_id"),inverseJoinColumns = @JoinColumn(name = "store_id"))
    private List<Category>categories;
}

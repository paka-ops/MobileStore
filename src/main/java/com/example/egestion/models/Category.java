package com.example.egestion.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
@Entity
@Data
@NoArgsConstructor
public class Category {
    @Id @GeneratedValue @UuidGenerator(style = UuidGenerator.Style.TIME)
    private UUID id;
    private String name;
    @OneToMany(mappedBy = "category",orphanRemoval = true)
    @JsonIgnore
    private List<Product> products = new ArrayList<>();
    private String description;
    @ManyToOne
    private Store store;

    public Category(List<Product> products, String name,String description, Store store) {
        this.products = products;
        this.name = name;
        this.store = store;
        this.description = description;
    }
}

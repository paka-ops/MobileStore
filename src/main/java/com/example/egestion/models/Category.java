package com.example.egestion.models;

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
    private List<Product> products = new ArrayList<>();
    @ManyToMany
    private List<Store> stores =  new ArrayList<>();

    public Category(List<Product> products, String name,List<Store> stores) {
        this.products = products;
        this.name = name;
        this.stores = stores;

    }
}

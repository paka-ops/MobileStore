package com.example.egestion.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ManyToAny;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;
@Entity
@Data @NoArgsConstructor
public class Product {
    @Id @GeneratedValue @UuidGenerator(style = UuidGenerator.Style.TIME)
    private UUID id;
    private String name;
    private double quantity;
    @Column(nullable = true)
    private double buyingPrice;
    @Column(nullable = false)
    private double salingPrice;
    @ManyToOne
    private Category category;
}


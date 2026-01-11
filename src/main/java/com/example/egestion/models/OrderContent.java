package com.example.egestion.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Entity
@NoArgsConstructor @AllArgsConstructor
public class OrderContent {
    @Id
    @GeneratedValue  @UuidGenerator(style= UuidGenerator.Style.TIME)
    private UUID id;
    private Double quantity;
    @OneToOne
    private Product product;
    @ManyToOne
    private Order order;

}

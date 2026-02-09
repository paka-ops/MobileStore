package com.example.egestion.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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
    @JsonIgnore
    private Product product;
    @ManyToOne
    private Order order;

}

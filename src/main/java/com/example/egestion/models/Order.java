package com.example.egestion.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;

@Data
@NoArgsConstructor
@Entity
@Table(name = "s_order")
public class Order {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToMany(mappedBy = "order")
    @MapKey(name="product")
    private Map<Product,OrderContent> products  = new TreeMap<>();
    private Date creationDate;
    @ManyToOne
    private Person maker;

}

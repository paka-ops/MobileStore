package com.example.egestion.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

import java.util.*;

@Data
@NoArgsConstructor
@Entity
@Table(name = "s_order")
public class Order {
    @Id @GeneratedValue @UuidGenerator(style=UuidGenerator.Style.TIME)
    private UUID id;
    @OneToMany(mappedBy = "order")
    @MapKey(name="product")
    private Map<Product,OrderContent> products  = new TreeMap<>();
    private Date creationDate;
    @ManyToOne
    private Person maker;

}

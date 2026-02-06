package com.example.egestion.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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
    @JsonIgnore
    @MapKey(name="product")
    private Map<Product,OrderContent> products  = new TreeMap<>();
    private Date creationDate;
    @ManyToOne
    private Person maker;

}

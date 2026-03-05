package com.example.egestion.models;

import com.example.egestion.repositories.OrderContentRepository;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.ws.rs.DefaultValue;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ManyToAny;
import org.hibernate.annotations.UuidGenerator;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;
@Entity
@Data @NoArgsConstructor
@Component
public class Product {
    @Id @GeneratedValue @UuidGenerator(style = UuidGenerator.Style.TIME)
    private UUID id;
    private String name;
    private double quantity;
    @Column(nullable = true)
    private double buyingPrice;
    @Column(nullable = false)
    private double salingPrice;
    @JsonIgnore
    @ManyToOne
    private Category category;
    @OneToOne()
    private Stock stock;
    @DefaultValue("false")
    private boolean deleted;

}


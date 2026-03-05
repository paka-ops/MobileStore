package com.example.egestion.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDate;
import java.util.Date;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
public class Stock {
    @Id @UuidGenerator(style = UuidGenerator.Style.TIME)
    private UUID id;
    private double totalSell;
    private double baseStock;
    @Column(nullable = true)
    private double buyingPrice;
    @Column(nullable = true)
    private double SellingPrice;
    private LocalDate date;
    private UUID productId;

}

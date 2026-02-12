package com.example.egestion.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderContentDto {
    private UUID productId;
    private UUID orderId;
    private double quantity;

}

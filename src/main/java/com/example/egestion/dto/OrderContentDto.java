package com.example.egestion.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderContentDto {
    private UUID id;
    private UUID productId;
    private UUID orderId;
    private double quantity;

    public OrderContentDto(UUID productId, UUID orderId, double quantity) {
        this.productId = productId;
        this.orderId = orderId;
        this.quantity = quantity;
    }
}

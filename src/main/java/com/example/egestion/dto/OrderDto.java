package com.example.egestion.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;
import java.util.UUID;
@Data
@AllArgsConstructor
public class OrderDto {
    private UUID makerId;
    private UUID storeId;
    private Map<UUID,UUID> products;
}

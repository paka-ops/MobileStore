package com.example.egestion.repositories;

import com.example.egestion.models.Stock;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface StockRepository extends JpaRepository<Stock, UUID> {
    Stock findByProductId(UUID productId);
    List<Stock> findAllByProductIdIn (List<UUID> productId);
}

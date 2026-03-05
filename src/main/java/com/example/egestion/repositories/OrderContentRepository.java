package com.example.egestion.repositories;

import com.example.egestion.models.OrderContent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public interface OrderContentRepository extends JpaRepository<OrderContent, UUID> {
   List<OrderContent> findAllByOrderId(UUID orderId);
   boolean deleteByProductId(UUID productId);
}

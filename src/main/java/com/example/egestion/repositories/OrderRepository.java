package com.example.egestion.repositories;

import com.example.egestion.models.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order,UUID> {
   List<Order> findAllByMaker(UUID makerId);
   boolean existsOrderByStore(UUID storeId);
   List<Order> findAllByStoreId(UUID storeId);
   Order findByIdAndStoreId(UUID orderId, UUID storeId);
}

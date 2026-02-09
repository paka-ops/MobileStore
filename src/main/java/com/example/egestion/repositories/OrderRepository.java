package com.example.egestion.repositories;

import com.example.egestion.models.Order;
import com.example.egestion.models.Person;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order,UUID> {
   List<Order> findAllByMaker(UUID makerId);
   boolean exitsOrderByStore(UUID storeId);
   List<Order> findAllByStore(UUID storeId);
   Order findOrderByStore(UUID orderId,UUID storeId);
}

package com.example.egestion.repositories;

import com.example.egestion.models.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order,UUID> {
   List<Order> findAllByMakerId(UUID makerId);
   boolean existsOrderByStoreId(UUID storeId);
   List<Order> findAllByStoreId(UUID storeId);
   Order findByIdAndStoreId(UUID orderId, UUID storeId);
   List<Order> getAllByStoreIdAndCreationDateBetween(UUID storeId,Date creationDateBefore, Date creationDateAfter);
   List<Order> getAllByStoreIdAndCreationDate(UUID storeId, Date date);
}

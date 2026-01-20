package com.example.egestion.repositories;

import com.example.egestion.models.Order;
import com.example.egestion.models.Person;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order,Long> {
   List<Order> findAllByMaker(Person person);
}

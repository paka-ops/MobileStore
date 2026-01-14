package com.example.egestion.services.interfaces;

import com.example.egestion.exceptions.ElementNotFoundException;
import com.example.egestion.exceptions.UpdateFailedException;
import com.example.egestion.models.Order;

import java.util.List;
import java.util.UUID;

public interface IOrder {
    Order create(Order order );
    Order update(Order order,UUID id);
    void delete(UUID id)throws UpdateFailedException;
    List<Order> getAll();
    Order get(UUID id) throws ElementNotFoundException;
}

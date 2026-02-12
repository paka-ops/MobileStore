package com.example.egestion.services.interfaces;

import com.example.egestion.dto.OrderDto;
import com.example.egestion.exceptions.*;
import com.example.egestion.models.Order;

import java.util.List;
import java.util.UUID;

public interface IOrder {
    Order create(Order order, UUID storeId ) throws CreationFailedException, NotAuthenticatedException, AccessDeniedException;
    Order update(Order order,UUID id) throws UpdateFailedException, ElementNotFoundException, AccessDeniedException, NotAuthenticatedException;
    void delete(UUID id)throws NotAuthenticatedException, AccessDeniedException, NotAuthorizedException,ElementNotFoundException;
    List<Order> getAllByStore(UUID storeId) throws ElementNotFoundException;
    List<Order> getAllByEmployee(UUID employeeId) throws ElementNotFoundException;
    Order getByStore(UUID orderId,UUID storeId);


}

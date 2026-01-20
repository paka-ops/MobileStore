package com.example.egestion.services.implementations;

import com.example.egestion.exceptions.*;
import com.example.egestion.models.Employee;
import com.example.egestion.models.Order;
import com.example.egestion.repositories.OrderRepository;
import com.example.egestion.security.SecurityCheck;
import com.example.egestion.services.interfaces.IOrder;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class OrderService implements IOrder {
    private final OrderRepository orderRepository;
    private final SecurityCheck secCheck;
    private final EmployeeService employeeService;
    public OrderService(OrderRepository orderRepository,SecurityCheck secCheck,EmployeeService employeeService){
        this.secCheck = secCheck;
        this.orderRepository = orderRepository;
        this.employeeService= employeeService;
    }


    @Override
    public Order create(Order order) throws CreationFailedException, NotAuthenticatedException, AccessDeniedException {*
        this.secCheck.hasRole("ROLE_EMPLOYEE");
        try{
            Order o = orderRepository.save(order);
            return o;
        }catch (Exception e){
            throw new CreationFailedException("error during saving ");
        }

    }

    @Override
    public Order update(Order order, UUID id) throws UpdateFailedException, ElementNotFoundException, AccessDeniedException, NotAuthenticatedException {
        this.secCheck.hasRole("ROLE_EMPLOYEE");
        boolean isExist = orderRepository.existsById(id);
        if(!isExist) throw new ElementNotFoundException("element not found");
        order.setId(id);
        Order o = orderRepository.save(order);
        return o;
    }

    @Override
    public void delete(UUID id) throws NotAuthenticatedException, AccessDeniedException, NotAuthorizedException, ElementNotFoundException {
        this.secCheck.hasRole("ROLE_EMPLOYER");
        boolean isExist = orderRepository.existsById(id);
        if(!isExist) throw new ElementNotFoundException("element not found");
        orderRepository.deleteById(id);
    }

    @Override
    public List<Order> getAll() throws UpdateFailedException, NotAuthenticatedException, AccessDeniedException, NotAuthorizedException, ElementNotFoundException {
        this.secCheck.hasRole("ROLE_EMPLOYER");
        return orderRepository.findAll();
    }

    @Override
    public List<Order> getAllByStore(UUID storeId) throws ElementNotFoundException {

        return List.of();
    }

    @Override
    public List<Order> getAllByEmployee(UUID employeeId) throws ElementNotFoundException {
        Employee em = employeeService.getOne(employeeId);
        return orderRepository.findAllByMaker(em);
    }

    @Override
    public Order get(UUID id) throws ElementNotFoundException {
       Optional<Order> order = orderRepository.findById(id);
       return order.orElseThrow(()-> new ElementNotFoundException("element not found  "));
    }
}

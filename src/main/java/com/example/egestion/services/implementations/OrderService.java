package com.example.egestion.services.implementations;

import com.example.egestion.enums.OrderStatus;
import com.example.egestion.exceptions.*;
import com.example.egestion.models.*;
import com.example.egestion.repositories.*;
import com.example.egestion.security.SecurityValidator;
import com.example.egestion.services.interfaces.IOrder;
import jakarta.transaction.Transactional;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class OrderService implements IOrder {
    private final OrderRepository orderRepository;
    private final SecurityValidator secCheck;
    private final EmployeeService employeeService;
    private final StoreRepository storeRepository;
    public OrderService(OrderRepository orderRepository, SecurityValidator secCheck, EmployeeService employeeService, StoreRepository storeRepository) {
        this.secCheck = secCheck;
        this.orderRepository = orderRepository;
        this.employeeService = employeeService;
        this.storeRepository = storeRepository;
    }

    @Override
    @PreAuthorize("(@securityValidator.hasRole('EMPLOYER') || @securityValidator.hasRole('EMPLOYEE')) ")
    @Transactional
    public Order create(Order order,UUID storeId) throws CreationFailedException, NotAuthenticatedException, AccessDeniedException {
        try{
            secCheck.validateStoreAccess(storeId);
            Store store = storeRepository.getReferenceById(storeId);
            Authentication auth = secCheck.getAuthentication();
            Person person = secCheck.findUserFromAuthentication(auth,Person.class);
            order.setStore(store);
            order.setMaker(person);
            order.setStatus(OrderStatus.CREATED);
            return orderRepository.save(order);
        }catch (Exception e){
            throw new CreationFailedException(e.getMessage());
        }

    }

    @Override
    @PreAuthorize("hasRole('EMPLOYEE')||hasRole('EMPLOYER')")
    @Transactional
    public Order update(Order order, UUID id) throws UpdateFailedException, ElementNotFoundException, AccessDeniedException, NotAuthenticatedException {
        Authentication auth = secCheck.getAuthentication();
        Class<?extends Person> T = (auth.getAuthorities().stream().anyMatch(a-> Objects.requireNonNull(a.getAuthority()).contains("EMPLOYER")))? Employer.class: Employee.class;
        Person employee= secCheck.  findUserFromAuthentication(auth,T);
        Order existingOrder = orderRepository.findById(id)
                .orElseThrow(()->new ElementNotFoundException("Order not found"));
        if(!existingOrder.getMaker().equals(employee)) throw new AccessDeniedException("You're not the order maker");
        order.setId(id);
        switch (order.getStatus()){
            case CREATED :
                order.setStatus(OrderStatus.VALIDATED);
            case VALIDATED:
                order.setStatus(OrderStatus.UPDATED);
        }
        return orderRepository.save(order);
    }

    @Override
    @PreAuthorize("hasRole('EMPLOYER')")
    public void delete(UUID id) throws NotAuthenticatedException, AccessDeniedException, NotAuthorizedException, ElementNotFoundException {
        Order order = orderRepository.findById(id)
                .orElseThrow(()->new ElementNotFoundException("Order not found"));
        Store store = order.getStore();
        secCheck.validateStoreAccess(store.getId());
        orderRepository.deleteById(id);
    }

    @Override
    @PreAuthorize("hasRole('EMPLOYER') || hasRole('EMPLOYEE')")
    public List<Order> getAllByStore(UUID storeId) throws ElementNotFoundException {
        secCheck.validateStoreAccess(storeId);
        return orderRepository.findAllByStoreId(storeId);
    }

    @Override
    @PreAuthorize("hasRole('EMPLOYER')")
    public List<Order> getAllByEmployee(UUID employeeId) throws ElementNotFoundException {
        Authentication auth = secCheck.getAuthentication();
        Employer employer = secCheck.findUserFromAuthentication(auth, Employer.class);
        secCheck.isEmployeeOfEmployer(employer.getId(),employeeId);
        return orderRepository.findAllByMakerId(employeeId);
    }
    @Override
    @PreAuthorize("hasRole('EMPLOYEE')||hasRole('EMPLOYER')")
    public Order getByStore(UUID orderId, UUID storeId) {
        secCheck.validateStoreAccess(storeId);
        boolean isOrderExist = orderRepository.existsById(orderId);
        if(!isOrderExist) throw new ElementNotFoundException("Order not found ");
        return orderRepository.findByIdAndStoreId(orderId,storeId);
    }

    @Override
    @PreAuthorize("hasRole('EMPLOYER') && hasRole('EMPLOYEE')")
    public List<Order> getAllByStoreBetweenDates(UUID storeId, Date startDate, Date endDate) throws ElementNotFoundException {
        secCheck.validateStoreAccess(storeId);
        if(endDate == null) return orderRepository.getAllByStoreIdAndCreationDate(storeId, startDate);
        return orderRepository.getAllByStoreIdAndCreationDateBetween(storeId,startDate,endDate);
    }


}

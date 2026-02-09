package com.example.egestion.services.implementations;

import com.example.egestion.exceptions.*;
import com.example.egestion.models.Employee;
import com.example.egestion.models.Employer;
import com.example.egestion.models.Order;
import com.example.egestion.models.Store;
import com.example.egestion.repositories.OrderRepository;
import com.example.egestion.repositories.StoreRepository;
import com.example.egestion.security.SecurityValidator;
import com.example.egestion.services.interfaces.IOrder;
import jakarta.transaction.Transactional;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
@Service
public class OrderService implements IOrder {
    private final OrderRepository orderRepository;
    private final SecurityValidator secCheck;
    private final EmployeeService employeeService;
    private final StoreRepository storeRepository;
    public OrderService(OrderRepository orderRepository, SecurityValidator secCheck, EmployeeService employeeService, StoreRepository storeRepository){
        this.secCheck = secCheck;
        this.orderRepository = orderRepository;
        this.employeeService= employeeService;
        this.storeRepository = storeRepository;
    }


    @Override
    @PreAuthorize("(@securityValidator.hasRole('EMPLOYER') || @securityValidator.hasRole('EMPLOYEE')) ")
    @Transactional
    public Order create(Order order,UUID storeId) throws CreationFailedException, NotAuthenticatedException, AccessDeniedException {

        try{
            secCheck.validateStoreAccess(storeId);
            Store store = storeRepository.getReferenceById(storeId);
            order.setStore(store);
            Order o = orderRepository.save(order);
            return o;
        }catch (Exception e){
            throw new CreationFailedException("error during saving ");
        }

    }

    @Override
    @PreAuthorize("hasRole('EMPLOYEE')")
    @Transactional
    public Order update(Order order, UUID id) throws UpdateFailedException, ElementNotFoundException, AccessDeniedException, NotAuthenticatedException {
        Authentication auth = secCheck.getAuthentication();
        Employee employee= secCheck.findUserFromAuthentication(auth,Employee.class);
        Order existingOrder = orderRepository.findById(id)
                .orElseThrow(()->new ElementNotFoundException("Order not found"));
        if(!existingOrder.getMaker().equals(employee)) throw new AccessDeniedException("You're not the order maker");
        order.setId(id);
        Order o = orderRepository.save(order);
        return o;
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
        return orderRepository.findAllByStore(storeId);
    }

    @Override
    @PreAuthorize("hasRole('EMPLOYER')")
    public List<Order> getAllByEmployee(UUID employeeId) throws ElementNotFoundException {
        Authentication auth = secCheck.getAuthentication();
        Employer employer = secCheck.findUserFromAuthentication(auth, Employer.class);
        secCheck.isEmployeeOfEmployer(employer.getId(),employeeId);
        return orderRepository.findAllByMaker(employeeId);
    }
    @Override
    @PreAuthorize("hasRole('EMPLOYEE')||hasRole('EMPLOYER')")
    public Order getByStore(UUID orderId, UUID storeId) {
        secCheck.validateStoreAccess(storeId);
        return orderRepository.findOrderByStore(orderId,storeId);
    }


}

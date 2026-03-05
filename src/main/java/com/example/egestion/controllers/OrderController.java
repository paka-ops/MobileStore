package com.example.egestion.controllers;

import com.example.egestion.configuration.ResponseBuilder;
import com.example.egestion.dto.OrderDto;
import com.example.egestion.models.Order;
import com.example.egestion.repositories.OrderRepository;
import com.example.egestion.services.implementations.OrderService;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {
    private final OrderService orderService;
    private final ResponseBuilder res;

    public OrderController(OrderService orderService, ResponseBuilder responseBuilder, OrderRepository orderRepository) {
        this.orderService = orderService;
        this.res = responseBuilder;
    }
    @GetMapping(params = {"storeId"})
    public ResponseEntity getAllByStore(@RequestParam UUID storeId){
        List<Order> orders = orderService.getAllByStore(storeId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(res.responseBody("OK","FOUND",orders));
    }
    @GetMapping(params = "employeeId")
    public ResponseEntity getAllByEmployee(@RequestParam UUID employeeId){
        List<Order> orders = orderService.getAllByEmployee(employeeId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(res.responseBody("OK","FOUND",orders));
    }
    @GetMapping(value = "/{orderId}",params = "storeId")
    public ResponseEntity getByStore(@PathVariable UUID orderId,@RequestParam UUID storeId){
        Order order = orderService.getByStore(orderId,storeId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(res.responseBody("OK","FOUND",order));
    }
    @PatchMapping(value = "/{orderId}")
    public ResponseEntity update(@PathVariable UUID orderId,@RequestBody Order order){
        Order o = orderService.update(order,orderId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(res.responseBody("OK","UPDATE SUCCESSFULLY",o));
    }
    @PostMapping(params = "storeId")
    public ResponseEntity create( @RequestParam UUID storeId){
        Order order = new Order();
        Order o = orderService.create(order,storeId);
        OrderDto orderDto = new OrderDto();
        orderDto.fromOrder(o);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(res.responseBody("OK","CREATION SUCCESSFULLY",o));
    }
    @DeleteMapping("/{orderId}")
    public ResponseEntity delete(@PathVariable UUID orderId) {
        orderService.delete(orderId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(res.responseBody("OK", "DELETION SUCCESSFULLY"));
    }
    @GetMapping(params = {"startDate","endDate","stordeId"})
    public ResponseEntity getOrderByDates(@RequestParam UUID storeId, @NotNull @RequestParam Date startDate, @PathVariable Date endDate ){
            List<Order> orders =  orderService.getAllByStoreBetweenDates(storeId,startDate,endDate);
            return ResponseEntity.ok().body(res.responseBody("200","FOUND",orders));

    }
}

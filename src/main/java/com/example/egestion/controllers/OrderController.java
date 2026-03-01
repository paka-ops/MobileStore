package com.example.egestion.controllers;

import com.example.egestion.configuration.ResponseBuilder;
import com.example.egestion.dto.OrderDto;
import com.example.egestion.models.Order;
import com.example.egestion.services.implementations.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {
    private final OrderService orderService;
    private final ResponseBuilder res;

    public OrderController(OrderService orderService, ResponseBuilder responseBuilder) {
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
    public ResponseEntity create(@RequestBody Order order, @RequestParam UUID storeId){
        Order o = orderService.create(order,storeId);
        OrderDto orderDto = new OrderDto();
        orderDto.fromOrder(o);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(res.responseBody("OK","CREATION SUCCESSFULLY",orderDto));
    }
    @DeleteMapping("/{orderId}")
    public ResponseEntity delete(@PathVariable UUID orderId){
        orderService.delete(orderId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(res.responseBody("OK","DELETION SUCCESSFULLY"));
    }
}

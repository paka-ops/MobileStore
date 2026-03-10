package com.example.egestion.controllers;

import com.example.egestion.configuration.ResponseBuilder;
import com.example.egestion.dto.OrderContentDto;
import com.example.egestion.dto.OrderContentResponse;
import com.example.egestion.dto.OrderDto;
import com.example.egestion.models.Order;
import com.example.egestion.models.OrderContent;
import com.example.egestion.repositories.OrderContentRepository;
import com.example.egestion.services.implementations.OrderContentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/orderContents")
public class OrderContentController {
    private final OrderContentService ocService;
    private final ResponseBuilder responseBuilder;
    private final OrderContentService orderContentService;

    public OrderContentController(OrderContentService ocService, ResponseBuilder responseBuilder, OrderContentService orderContentService, OrderContentService orderContentService1) {
        this.ocService = ocService;
        this.responseBuilder = responseBuilder;
        this.orderContentService = orderContentService1;
    }
    @PostMapping(params = "orderId")
    public ResponseEntity add(@RequestBody List<OrderContentDto> orderContentDtos, @RequestParam UUID orderId){
        Order order = ocService.add(orderContentDtos,orderId);
        OrderDto orderDto = new OrderDto();
        orderDto.fromOrder(order);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(responseBuilder.responseBody("CREATED","ORDER MAKING SUCCESSFULLY",order));

    }
    @GetMapping(params = "orderId")
    public ResponseEntity getContent(@RequestParam UUID orderId){
        List<OrderContent> contents = orderContentService.getAllByOrderId(orderId);
        List<OrderContentResponse> responses = new ArrayList<>(contents.size());
        OrderContentResponse response = new OrderContentResponse();
        contents.forEach(e->{
            response.fromOrderContent(e);
            responses.add(response);
        });
        return ResponseEntity.ok().body(responseBuilder.responseBody("FOUND","OrderContentFound",responses));
    }
    @PostMapping
    public ResponseEntity getContentByOrderIds(@RequestBody List<UUID> ordersIds){
        List<OrderContent> contents  =    orderContentService.getAllByOrderIds(ordersIds);
        List<OrderContentResponse> responses = new ArrayList<>(contents.size());
        OrderContentResponse response = new OrderContentResponse();
        contents.forEach(e->{
            response.fromOrderContent(e);
            responses.add(response);
        });
        return ResponseEntity.ok().body(responseBuilder.responseBody("FOUND","OrderContentFound",responses));
    }

}

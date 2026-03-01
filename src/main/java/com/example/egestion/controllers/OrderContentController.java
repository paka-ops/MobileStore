package com.example.egestion.controllers;

import com.example.egestion.configuration.ResponseBuilder;
import com.example.egestion.dto.OrderContentDto;
import com.example.egestion.dto.OrderDto;
import com.example.egestion.models.Order;
import com.example.egestion.repositories.OrderContentRepository;
import com.example.egestion.services.implementations.OrderContentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/orderContents")
public class OrderContentController {
    private final OrderContentService ocService;
    private final ResponseBuilder responseBuilder;

    public OrderContentController(OrderContentService ocService, ResponseBuilder responseBuilder) {
        this.ocService = ocService;
        this.responseBuilder = responseBuilder;
    }
    @PostMapping(params = "orderId")
    public ResponseEntity add(@RequestBody List<OrderContentDto> orderContentDtos, @RequestParam UUID orderId){
        Order order = ocService.add(orderContentDtos,orderId);
        OrderDto orderDto = new OrderDto();
        orderDto.fromOrder(order);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(responseBuilder.responseBody("CREATED","ORDER MAKING SUCCESSFULLY",orderDto));

    }

}

package com.example.egestion.services.interfaces;

import com.example.egestion.dto.OrderContentDto;
import com.example.egestion.models.Order;
import com.example.egestion.models.OrderContent;

import java.util.List;
import java.util.UUID;

public interface IOrderContent {
    public Order add(List<OrderContentDto> orderContents, UUID storeId);
    public OrderContent update(OrderContent orderContent, UUID orderId);
}

package com.example.egestion.services.interfaces;

import com.example.egestion.dto.OrderContentDto;
import com.example.egestion.models.Order;
import com.example.egestion.models.OrderContent;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;
import java.util.UUID;

public interface IOrderContent {
    public Order add(List<OrderContentDto> orderContents, UUID storeId);
    public OrderContent update(OrderContent orderContent, UUID orderId);
    boolean delete(UUID orderContentId);
    boolean deleteMany(@NotEmpty List<UUID> orderContentsIds);
    List<OrderContent> getAllByOrderId(UUID orderId);
    List<OrderContent> getAllByOrderIds(List<UUID> ordersIds);

}

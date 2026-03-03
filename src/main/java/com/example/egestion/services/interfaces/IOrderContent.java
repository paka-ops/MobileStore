package com.example.egestion.services.interfaces;

import com.example.egestion.dto.OrderContentDto;
import com.example.egestion.models.Order;
import com.example.egestion.models.OrderContent;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;
import java.util.UUID;

public interface IOrderContent {
    public Order add(List<OrderContentDto> orderContents, UUID storeId);
    public OrderContent update(OrderContent orderContent, UUID orderId);
    boolean delete(UUID orderContentId);
    boolean deleteMany(@NotEmpty List<UUID> orderContentsIds);
}

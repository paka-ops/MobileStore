package com.example.egestion.dto;

import com.example.egestion.enums.OrderStatus;
import com.example.egestion.models.Order;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
@Data
@NoArgsConstructor
public class OrderDto {
    private UUID orderId;
    private UUID makerId;
    private UUID storeId;
    private Map<UUID,UUID> products;
    private Date createdAt;
    private OrderStatus status;
    public  void fromOrder(Order order){
        orderId = order.getId();
        makerId = order.getMaker().getId();
        storeId = order.getStore().getId();
        status = order.getStatus();
        Map<UUID,UUID> orderProductUUID = new HashMap<>(order.getProducts().size());
        order.getProducts().forEach((p,oc)->{
            orderProductUUID.put(p.getId(),oc.getId());
        });
        products = orderProductUUID;
        createdAt = order.getCreationDate();

    }
}

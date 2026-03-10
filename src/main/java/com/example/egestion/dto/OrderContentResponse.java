package com.example.egestion.dto;

import com.example.egestion.models.OrderContent;
import com.example.egestion.models.Product;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;
@Data
@NoArgsConstructor
public class OrderContentResponse {
    private UUID id;
    private double quantity;
    private ProductDto productDto;
    private UUID orderId;

    @AllArgsConstructor
    @Data
    private static class ProductDto{
        private UUID id;
        private String name;
        private double salingPrice;
        private double buyingPrice;
        private CategoryDto categoryDto;
    }
    @AllArgsConstructor
    @Data
    private static class CategoryDto{
        private UUID id;
        private String name;
    }
    public void fromOrderContent(OrderContent orderContent){
        Product product = orderContent.getProduct();
        this.id = orderContent.getId();
        this.quantity = orderContent.getQuantity();
        CategoryDto categoryDto = new CategoryDto(product.getCategory().getId(),product.getCategory().getName());
        this.productDto = new ProductDto(product.getId(),product.getName(),product.getSalingPrice(), product.getBuyingPrice(),categoryDto);
        this.orderId  = orderContent.getOrder().getId();
    }

}

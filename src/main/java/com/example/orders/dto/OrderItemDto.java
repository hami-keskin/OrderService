package com.example.orders.dto;

import lombok.Data;

@Data
public class OrderItemDto {
    private Integer id;
    private Integer orderId;
    private Integer productId;
    private Integer quantity;
    private Double price;
}

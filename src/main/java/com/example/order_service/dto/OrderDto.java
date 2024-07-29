package com.example.order_service.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderDto {
    private Integer id;
    private LocalDateTime orderDate;
    private String status;
    private Integer customerId;
    private Integer shippingAddressId;
    private Double totalAmount;
    private List<OrderItemDto> orderItems;
}

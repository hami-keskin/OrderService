package com.example.orders.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OrderDto {
    private Integer id;
    private LocalDateTime orderDate;
    private String status;
    private Integer customerId;
    private Integer shippingAddressId;
    private Double totalAmount;
}

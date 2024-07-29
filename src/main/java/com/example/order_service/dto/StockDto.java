package com.example.order_service.dto;

import lombok.Data;

@Data
public class StockDto {
    private Integer id;
    private Integer productId;
    private Integer quantity;
}

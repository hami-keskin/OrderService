package com.example.orders.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class InvoiceDto {
    private Integer id;
    private Integer orderId;
    private LocalDateTime invoiceDate;
    private Double totalAmount;
}

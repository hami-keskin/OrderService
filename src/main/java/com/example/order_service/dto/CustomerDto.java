package com.example.order_service.dto;

import lombok.Data;

@Data
public class CustomerDto {
    private Integer id;
    private String name;
    private String email;
    private String phone;
}

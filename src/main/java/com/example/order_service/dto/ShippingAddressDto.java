package com.example.order_service.dto;

import lombok.Data;

@Data
public class ShippingAddressDto {
    private Integer id;
    private Integer customerId;
    private String addressLine1;
    private String addressLine2;
    private String city;
    private String state;
    private String postalCode;
    private String country;
}

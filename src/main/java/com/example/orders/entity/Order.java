package com.example.orders.entity;

import lombok.Data;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private LocalDateTime orderDate;

    private String status;

    private Integer customerId;

    private Integer shippingAddressId;

    private Double totalAmount;
}

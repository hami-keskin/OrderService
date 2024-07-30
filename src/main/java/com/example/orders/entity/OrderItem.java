package com.example.orders.entity;

import lombok.Data;
import jakarta.persistence.*;

@Entity
@Data
@Table(name = "order_items")
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer orderId;
    private Integer productId;
    private Integer quantity;
    private Double price;
}

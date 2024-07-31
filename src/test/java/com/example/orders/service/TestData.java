package com.example.orders.service;

import com.example.orders.dto.OrderDto;
import com.example.orders.dto.OrderItemDto;
import com.example.orders.entity.Order;
import com.example.orders.entity.OrderItem;
import com.example.orders.mapper.OrderMapper;
import com.example.orders.mapper.OrderItemMapper;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class TestData {
    public static Order createOrder() {
        Order order = new Order();
        order.setId(1);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus("PENDING");
        order.setTotalAmount(100.0);
        order.setCustomerId(1);
        return order;
    }

    public static OrderDto createOrderDto() {
        return OrderMapper.INSTANCE.toDto(createOrder());
    }

    public static OrderItem createOrderItem() {
        OrderItem orderItem = new OrderItem();
        orderItem.setId(1);
        orderItem.setProductId(1);
        orderItem.setQuantity(2);
        orderItem.setPrice(50.0);
        orderItem.setOrder(createOrder());
        return orderItem;
    }

    public static OrderItemDto createOrderItemDto() {
        return OrderItemMapper.INSTANCE.toDto(createOrderItem());
    }

    public static Map<String, Object> createProduct() {
        Map<String, Object> product = new HashMap<>();
        product.put("price", 50.0);
        return product;
    }
}

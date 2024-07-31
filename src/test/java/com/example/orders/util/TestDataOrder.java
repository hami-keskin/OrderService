package com.example.orders.util;

import com.example.orders.dto.OrderDto;
import com.example.orders.entity.Order;

import java.time.LocalDateTime;

public class TestDataOrder {

    public static Order createOrder() {
        Order order = new Order();
        order.setId(1);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus("NEW");
        order.setTotalAmount(100.0);
        order.setCustomerId(1);
        return order;
    }

    public static OrderDto createOrderDto() {
        OrderDto orderDto = new OrderDto();
        orderDto.setId(1);
        orderDto.setOrderDate(LocalDateTime.now());
        orderDto.setStatus("NEW");
        orderDto.setTotalAmount(100.0);
        orderDto.setCustomerId(1);
        return orderDto;
    }
}

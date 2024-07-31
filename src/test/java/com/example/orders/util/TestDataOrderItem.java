package com.example.orders.util;

import com.example.orders.dto.OrderItemDto;
import com.example.orders.entity.Order;
import com.example.orders.entity.OrderItem;

import java.time.LocalDateTime;

public class TestDataOrderItem {

    public static Order createOrder() {
        Order order = new Order();
        order.setId(1);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus("NEW");
        order.setTotalAmount(100.0);
        order.setCustomerId(1);
        return order;
    }

    public static OrderItem createOrderItem(Order order) {
        OrderItem orderItem = new OrderItem();
        orderItem.setId(1);
        orderItem.setProductId(1);
        orderItem.setQuantity(1);
        orderItem.setPrice(100.0);
        orderItem.setOrder(order);
        return orderItem;
    }

    public static OrderItemDto createOrderItemDto() {
        OrderItemDto orderItemDto = new OrderItemDto();
        orderItemDto.setId(1);
        orderItemDto.setProductId(1);
        orderItemDto.setQuantity(1);
        orderItemDto.setOrderId(1);
        return orderItemDto;
    }
}

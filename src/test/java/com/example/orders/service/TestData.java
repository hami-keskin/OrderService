package com.example.orders.service;

import com.example.orders.dto.OrderDto;
import com.example.orders.dto.OrderItemDto;
import com.example.orders.entity.Order;
import com.example.orders.entity.OrderItem;
import com.example.orders.mapper.OrderItemMapper;
import com.example.orders.mapper.OrderMapper;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestData {

    public static Order createOrder(Integer id, LocalDateTime orderDate, String status, Double totalAmount, Integer customerId, List<OrderItem> orderItems) {
        Order order = new Order();
        order.setId(id);
        order.setOrderDate(orderDate);
        order.setStatus(status);
        order.setTotalAmount(totalAmount);
        order.setCustomerId(customerId);
        order.setOrderItems(orderItems);
        return order;
    }

    public static OrderItem createOrderItem(Integer id, Integer productId, Integer quantity, Double price, Order order) {
        OrderItem orderItem = new OrderItem();
        orderItem.setId(id);
        orderItem.setProductId(productId);
        orderItem.setQuantity(quantity);
        orderItem.setPrice(price);
        orderItem.setOrder(order);
        return orderItem;
    }

    public static OrderDto createOrderDto() {
        Order order = createOrder(1, LocalDateTime.now(), "NEW", 100.0, 1, null);
        return OrderMapper.INSTANCE.toDto(order);
    }

    public static OrderItemDto createOrderItemDto() {
        Order order = createOrder(1, LocalDateTime.now(), "NEW", 100.0, 1, null);
        OrderItem orderItem = createOrderItem(1, 1, 2, 50.0, order);
        OrderItemDto orderItemDto = OrderItemMapper.INSTANCE.toDto(orderItem);
        orderItemDto.setOrderId(order.getId()); // Ensure orderId is set
        return orderItemDto;
    }

    public static Map<String, Object> createProduct() {
        Map<String, Object> product = new HashMap<>();
        product.put("price", 50.0);
        return product;
    }
}

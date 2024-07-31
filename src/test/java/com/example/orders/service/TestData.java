package com.example.orders.service;

import com.example.orders.entity.Order;
import com.example.orders.entity.OrderItem;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

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

    public static List<Order> getSampleOrders() {
        Order order1 = createOrder(1, LocalDateTime.now(), "Processing", 100.0, 1, null);
        Order order2 = createOrder(2, LocalDateTime.now(), "Shipped", 200.0, 2, null);
        return Arrays.asList(order1, order2);
    }

    public static List<OrderItem> getSampleOrderItems(Order order) {
        OrderItem orderItem1 = createOrderItem(1, 101, 2, 50.0, order);
        OrderItem orderItem2 = createOrderItem(2, 102, 1, 150.0, order);
        return Arrays.asList(orderItem1, orderItem2);
    }
}

package com.example.orders.service;

import com.example.orders.client.ProductClient;
import com.example.orders.dto.OrderItemDto;
import com.example.orders.entity.Order;
import com.example.orders.entity.OrderItem;
import com.example.orders.mapper.OrderItemMapper;
import com.example.orders.repository.OrderItemRepository;
import com.example.orders.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
public class OrderItemService {

    @Autowired
    OrderItemRepository orderItemRepository;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    ProductClient productClient;

    @Cacheable("orderItems")
    public OrderItemDto getOrderItemById(Integer id) {
        return orderItemRepository.findById(id)
                .map(OrderItemMapper.INSTANCE::toDto)
                .orElse(null);
    }

    @Transactional
    @CachePut(value = "orderItems", key = "#orderItemDto.id")
    public OrderItemDto createOrderItem(OrderItemDto orderItemDto) {
        if (orderItemDto.getProductId() <= 0) {
            throw new IllegalArgumentException("Product ID must be positive");
        }

        Map<String, Object> product = productClient.getProductById(orderItemDto.getProductId());
        Double price = (Double) product.get("price");

        OrderItem orderItem = OrderItemMapper.INSTANCE.toEntity(orderItemDto);
        orderItem.setPrice(price);
        Order order = orderRepository.findById(orderItemDto.getOrderId()).orElseThrow(() -> new RuntimeException("Order not found"));
        orderItem.setOrder(order);
        return OrderItemMapper.INSTANCE.toDto(orderItemRepository.save(orderItem));
    }

    @Transactional
    @CachePut(value = "orderItems", key = "#orderItemDto.id")
    public OrderItemDto updateOrderItem(OrderItemDto orderItemDto) {
        OrderItem orderItem = orderItemRepository.findById(orderItemDto.getId()).orElseThrow();
        Order order = orderRepository.findById(orderItemDto.getOrderId()).orElseThrow(() -> new RuntimeException("Order not found"));
        orderItem.setOrder(order);

        Map<String, Object> product = productClient.getProductById(orderItemDto.getProductId());
        Double price = (Double) product.get("price");

        orderItem.setProductId(orderItemDto.getProductId());
        orderItem.setQuantity(orderItemDto.getQuantity());
        orderItem.setPrice(price);
        return OrderItemMapper.INSTANCE.toDto(orderItemRepository.save(orderItem));
    }

    @Transactional
    @CacheEvict(value = "orderItems", key = "#id")
    public void deleteOrderItem(Integer id) {
        orderItemRepository.deleteById(id);
    }
}

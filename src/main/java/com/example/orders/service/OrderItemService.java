package com.example.orders.service;

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

@Service
public class OrderItemService {

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Cacheable("orderItems")
    public OrderItemDto getOrderItemById(Integer id) {
        return orderItemRepository.findById(id)
                .map(OrderItemMapper.INSTANCE::toDto)
                .orElse(null);
    }

    @Transactional
    @CachePut(value = "orderItems", key = "#orderItemDto.id")
    public OrderItemDto createOrderItem(OrderItemDto orderItemDto) {
        OrderItem orderItem = OrderItemMapper.INSTANCE.toEntity(orderItemDto);
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
        orderItem.setProductId(orderItemDto.getProductId());
        orderItem.setQuantity(orderItemDto.getQuantity());
        orderItem.setPrice(orderItemDto.getPrice());
        return OrderItemMapper.INSTANCE.toDto(orderItemRepository.save(orderItem));
    }

    @Transactional
    @CacheEvict(value = "orderItems", key = "#id")
    public void deleteOrderItem(Integer id) {
        orderItemRepository.deleteById(id);
    }
}
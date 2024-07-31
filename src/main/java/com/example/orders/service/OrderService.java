package com.example.orders.service;

import com.example.orders.dto.OrderDto;
import com.example.orders.entity.Order;
import com.example.orders.mapper.OrderMapper;
import com.example.orders.repository.OrderRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Cacheable("orders")
    public OrderDto getOrderById(Integer id) {
        return orderRepository.findById(id)
                .map(OrderMapper.INSTANCE::toDto)
                .orElseThrow(() -> new RuntimeException("Order not found"));
    }

    @Transactional
    @CachePut(value = "orders", key = "#orderDto.id")
    public OrderDto createOrder(OrderDto orderDto) {
        Order order = OrderMapper.INSTANCE.toEntity(orderDto);
        return OrderMapper.INSTANCE.toDto(orderRepository.save(order));
    }

    @Transactional
    @CachePut(value = "orders", key = "#orderDto.id")
    public OrderDto updateOrder(OrderDto orderDto) {
        Order order = orderRepository.findById(orderDto.getId()).orElseThrow();
        order.setOrderDate(orderDto.getOrderDate());
        order.setStatus(orderDto.getStatus());
        order.setTotalAmount(orderDto.getTotalAmount());
        order.setCustomerId(orderDto.getCustomerId());
        return OrderMapper.INSTANCE.toDto(orderRepository.save(order));
    }

    @Transactional
    @CacheEvict(value = "orders", key = "#id")
    public void deleteOrder(Integer id) {
        orderRepository.deleteById(id);
    }
}

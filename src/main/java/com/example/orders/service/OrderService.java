package com.example.orders.service;

import com.example.orders.dto.OrderDto;
import com.example.orders.entity.Order;
import com.example.orders.mapper.OrderMapper;
import com.example.orders.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

    @Cacheable("order")
    public Optional<OrderDto> getOrderById(Integer id) {
        return orderRepository.findById(id)
                .map(orderMapper::toDto);
    }

    @CachePut(value = "order", key = "#result.id")
    public OrderDto createOrder(OrderDto orderDto) {
        Order order = orderMapper.toEntity(orderDto);
        order = orderRepository.save(order);
        return orderMapper.toDto(order);
    }

    @CachePut(value = "order", key = "#orderDto.id")
    public OrderDto updateOrder(OrderDto orderDto) {
        Order order = orderMapper.toEntity(orderDto);
        order = orderRepository.save(order);
        return orderMapper.toDto(order);
    }

    @CacheEvict(value = "order", key = "#id")
    public void deleteOrder(Integer id) {
        orderRepository.deleteById(id);
    }
}

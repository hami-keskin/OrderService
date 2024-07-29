package com.example.orders.service;

import com.example.orders.dto.OrderDto;
import com.example.orders.entity.Order;
import com.example.orders.mapper.OrderMapper;
import com.example.orders.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Cacheable("orders")
    public List<OrderDto> getAllOrders() {
        return orderRepository.findAll().stream()
                .map(OrderMapper.INSTANCE::toDto)
                .collect(Collectors.toList());
    }

    @Cacheable(value = "orders", key = "#id")
    public OrderDto getOrderById(Integer id) {
        return orderRepository.findById(id)
                .map(OrderMapper.INSTANCE::toDto)
                .orElse(null);
    }

    @Transactional
    @CacheEvict(value = "orders", allEntries = true)
    public OrderDto createOrder(OrderDto orderDto) {
        Order order = OrderMapper.INSTANCE.toEntity(orderDto);
        return OrderMapper.INSTANCE.toDto(orderRepository.save(order));
    }

    @Transactional
    @CachePut(value = "orders", key = "#id")
    public OrderDto updateOrder(Integer id, OrderDto orderDto) {
        Order order = orderRepository.findById(id).orElseThrow();
        order.setOrderDate(orderDto.getOrderDate());
        order.setStatus(orderDto.getStatus());
        order.setCustomerId(orderDto.getCustomerId());
        order.setTotalAmount(orderDto.getTotalAmount());
        return OrderMapper.INSTANCE.toDto(orderRepository.save(order));
    }

    @Transactional
    @CacheEvict(value = "orders", allEntries = true)
    public void deleteOrder(Integer id) {
        orderRepository.deleteById(id);
    }
}

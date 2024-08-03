package com.example.orders.service;

import com.example.orders.dto.OrderItemDto;
import com.example.orders.entity.OrderItem;
import com.example.orders.mapper.OrderItemMapper;
import com.example.orders.repository.OrderItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderItemService {
    private final OrderItemRepository orderItemRepository;
    private final OrderItemMapper orderItemMapper;

    @Cacheable("orderItem")
    public Optional<OrderItemDto> getOrderItemById(Integer id) {
        return orderItemRepository.findById(id)
                .map(orderItemMapper::toDto);
    }

    @CachePut(value = "orderItem", key = "#result.id")
    public OrderItemDto createOrderItem(OrderItemDto orderItemDto) {
        OrderItem orderItem = orderItemMapper.toEntity(orderItemDto);
        orderItem = orderItemRepository.save(orderItem);
        return orderItemMapper.toDto(orderItem);
    }

    @CachePut(value = "orderItem", key = "#orderItemDto.id")
    public OrderItemDto updateOrderItem(OrderItemDto orderItemDto) {
        OrderItem orderItem = orderItemMapper.toEntity(orderItemDto);
        orderItem = orderItemRepository.save(orderItem);
        return orderItemMapper.toDto(orderItem);
    }

    @CacheEvict(value = "orderItem", key = "#id")
    public void deleteOrderItem(Integer id) {
        orderItemRepository.deleteById(id);
    }
}

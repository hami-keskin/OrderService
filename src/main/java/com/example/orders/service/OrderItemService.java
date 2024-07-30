package com.example.orders.service;

import com.example.orders.dto.OrderItemDto;
import com.example.orders.entity.OrderItem;
import com.example.orders.mapper.OrderItemMapper;
import com.example.orders.repository.OrderItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderItemService {

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private OrderItemMapper orderItemMapper;

    public List<OrderItemDto> getAllOrderItems() {
        return orderItemRepository.findAll().stream()
                .map(orderItemMapper::toOrderItemDto)
                .collect(Collectors.toList());
    }

    public OrderItemDto getOrderItemById(Integer id) {
        return orderItemRepository.findById(id)
                .map(orderItemMapper::toOrderItemDto)
                .orElse(null);
    }

    public OrderItemDto createOrderItem(OrderItemDto orderItemDto) {
        OrderItem orderItem = orderItemMapper.toOrderItem(orderItemDto);
        orderItem = orderItemRepository.save(orderItem);
        return orderItemMapper.toOrderItemDto(orderItem);
    }

    public OrderItemDto updateOrderItem(Integer id, OrderItemDto orderItemDto) {
        return orderItemRepository.findById(id)
                .map(existingOrderItem -> {
                    existingOrderItem.setOrderId(orderItemDto.getOrderId());
                    existingOrderItem.setProductId(orderItemDto.getProductId());
                    existingOrderItem.setQuantity(orderItemDto.getQuantity());
                    existingOrderItem.setPrice(orderItemDto.getPrice());
                    return orderItemMapper.toOrderItemDto(orderItemRepository.save(existingOrderItem));
                })
                .orElse(null);
    }

    public boolean deleteOrderItem(Integer id) {
        if (orderItemRepository.existsById(id)) {
            orderItemRepository.deleteById(id);
            return true;
        }
        return false;
    }
}

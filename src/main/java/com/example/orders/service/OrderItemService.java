package com.example.orders.service;

import com.example.orders.dto.OrderItemDto;
import com.example.orders.entity.OrderItem;
import com.example.orders.entity.Order;
import com.example.orders.mapper.OrderItemMapper;
import com.example.orders.repository.OrderItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderItemService {

    @Autowired
    private OrderItemRepository orderItemRepository;

    public List<OrderItemDto> getAllOrderItems() {
        return orderItemRepository.findAll().stream()
                .map(OrderItemMapper.INSTANCE::toDto)
                .collect(Collectors.toList());
    }

    public OrderItemDto getOrderItemById(Integer id) {
        return orderItemRepository.findById(id)
                .map(OrderItemMapper.INSTANCE::toDto)
                .orElse(null);
    }

    @Transactional
    public OrderItemDto createOrderItem(OrderItemDto orderItemDto) {
        OrderItem orderItem = OrderItemMapper.INSTANCE.toEntity(orderItemDto);
        return OrderItemMapper.INSTANCE.toDto(orderItemRepository.save(orderItem));
    }

    @Transactional
    public OrderItemDto updateOrderItem(Integer id, OrderItemDto orderItemDto) {
        OrderItem orderItem = orderItemRepository.findById(id).orElseThrow();
        Order order = new Order();
        order.setId(orderItemDto.getOrderId());
        orderItem.setOrder(order);
        orderItem.setProductId(orderItemDto.getProductId());
        orderItem.setQuantity(orderItemDto.getQuantity());
        orderItem.setPrice(orderItemDto.getPrice());
        return OrderItemMapper.INSTANCE.toDto(orderItemRepository.save(orderItem));
    }

    @Transactional
    public void deleteOrderItem(Integer id) {
        orderItemRepository.deleteById(id);
    }
}

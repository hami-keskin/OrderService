package com.example.orders.service;

import com.example.orders.dto.OrderDto;
import com.example.orders.entity.Order;
import com.example.orders.mapper.OrderMapper;
import com.example.orders.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderMapper orderMapper;

    public List<OrderDto> getAllOrders() {
        return orderRepository.findAll().stream()
                .map(orderMapper::toOrderDto)
                .collect(Collectors.toList());
    }

    public OrderDto getOrderById(Integer id) {
        return orderRepository.findById(id)
                .map(orderMapper::toOrderDto)
                .orElse(null);
    }

    public OrderDto createOrder(OrderDto orderDto) {
        Order order = orderMapper.toOrder(orderDto);
        order = orderRepository.save(order);
        return orderMapper.toOrderDto(order);
    }

    public OrderDto updateOrder(Integer id, OrderDto orderDto) {
        return orderRepository.findById(id)
                .map(existingOrder -> {
                    existingOrder.setOrderDate(orderDto.getOrderDate());
                    existingOrder.setStatus(orderDto.getStatus());
                    existingOrder.setCustomerId(orderDto.getCustomerId());
                    existingOrder.setShippingAddressId(orderDto.getShippingAddressId());
                    existingOrder.setTotalAmount(orderDto.getTotalAmount());
                    return orderMapper.toOrderDto(orderRepository.save(existingOrder));
                })
                .orElse(null);
    }

    public boolean deleteOrder(Integer id) {
        if (orderRepository.existsById(id)) {
            orderRepository.deleteById(id);
            return true;
        }
        return false;
    }
}

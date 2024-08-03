package com.example.orders.service;

import com.example.orders.client.ProductClient;
import com.example.orders.client.ProductDto;
import com.example.orders.dto.OrderDto;
import com.example.orders.dto.OrderItemDto;
import com.example.orders.entity.Order;
import com.example.orders.entity.OrderItem;
import com.example.orders.mapper.OrderItemMapper;
import com.example.orders.mapper.OrderMapper;
import com.example.orders.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;
    private final ProductClient productClient;

    @Cacheable("order")
    public Optional<OrderDto> getOrderById(Integer id) {
        return orderRepository.findById(id)
                .map(orderMapper::toDto);
    }

    @CachePut(value = "order", key = "#result.id")
    public OrderDto createOrder(OrderDto orderDto) {
        Order order = orderMapper.toEntity(orderDto);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus(1); // Burada örnek olarak 1 atanıyor, ihtiyaçlarınıza göre güncelleyebilirsiniz.
        order.setTotalAmount(0.0);
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

    @Transactional
    public OrderItemDto addOrderItem(Integer orderId, OrderItemDto orderItemDto) {
        Order order = orderRepository.findById(orderId).orElseThrow();
        OrderItem orderItem = orderItemMapper.toEntity(orderItemDto);

        ProductDto productDto = productClient.getProductById(orderItemDto.getProductId());
        orderItem.setPrice(productDto.getPrice());
        orderItem.setTotalAmount(productDto.getPrice() * orderItemDto.getQuantity());

        orderItem.setOrder(order);
        order.getOrderItems().add(orderItem);
        order.setTotalAmount(order.getTotalAmount() + orderItem.getTotalAmount());

        orderRepository.save(order);

        return orderItemMapper.toDto(orderItem);
    }
}

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
import com.example.orders.repository.OrderItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
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
        order.setStatus(1); // Örnek olarak 1 atanıyor, ihtiyaçlarınıza göre güncelleyebilirsiniz.
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

        // Aynı productId ile mevcut OrderItem'ı bul
        Optional<OrderItem> existingOrderItemOpt = order.getOrderItems()
                .stream()
                .filter(item -> item.getProductId().equals(orderItemDto.getProductId()))
                .findFirst();

        ProductDto productDto = productClient.getProductById(orderItemDto.getProductId());

        OrderItem orderItem;
        if (existingOrderItemOpt.isPresent()) {
            // Mevcut OrderItem'ı güncelle
            orderItem = existingOrderItemOpt.get();
            order.setTotalAmount(order.getTotalAmount() - orderItem.getTotalAmount());
            orderItem.setQuantity(orderItem.getQuantity() + orderItemDto.getQuantity());
            orderItem.setTotalAmount(productDto.getPrice() * orderItem.getQuantity());
        } else {
            // Yeni OrderItem oluştur
            orderItem = orderItemMapper.toEntity(orderItemDto);
            orderItem.setPrice(productDto.getPrice());
            orderItem.setTotalAmount(productDto.getPrice() * orderItemDto.getQuantity());
            orderItem.setOrder(order);
            order.getOrderItems().add(orderItem);
        }

        order.setTotalAmount(order.getTotalAmount() + orderItem.getTotalAmount());

        orderItem = orderItemRepository.save(orderItem); // OrderItem kaydediliyor ve id'si oluşturuluyor
        orderRepository.save(order);

        return orderItemMapper.toDto(orderItem);
    }

    @Transactional
    public OrderItemDto updateOrderItem(Integer orderId, Integer orderItemId, OrderItemDto orderItemDto) {
        Order order = orderRepository.findById(orderId).orElseThrow();
        OrderItem orderItem = orderItemRepository.findById(orderItemId).orElseThrow();

        if (!order.getOrderItems().contains(orderItem)) {
            throw new IllegalArgumentException("Order item does not belong to the specified order.");
        }

        if (orderItemDto.getQuantity() <= 0) {
            deleteOrderItem(orderId, orderItemId);
            return null;
        }

        ProductDto productDto = productClient.getProductById(orderItem.getProductId());
        order.setTotalAmount(order.getTotalAmount() - orderItem.getTotalAmount());
        orderItem.setQuantity(orderItemDto.getQuantity());
        orderItem.setTotalAmount(productDto.getPrice() * orderItem.getQuantity());
        order.setTotalAmount(order.getTotalAmount() + orderItem.getTotalAmount());

        orderItem = orderItemRepository.save(orderItem);
        orderRepository.save(order);

        return orderItemMapper.toDto(orderItem);
    }

    @Transactional
    public void deleteOrderItem(Integer orderId, Integer orderItemId) {
        Order order = orderRepository.findById(orderId).orElseThrow();
        OrderItem orderItem = orderItemRepository.findById(orderItemId).orElseThrow();

        if (!order.getOrderItems().contains(orderItem)) {
            throw new IllegalArgumentException("Order item does not belong to the specified order.");
        }

        order.setTotalAmount(order.getTotalAmount() - orderItem.getTotalAmount());
        order.getOrderItems().remove(orderItem);
        orderItemRepository.delete(orderItem);
        orderRepository.save(order);
    }

    @Transactional(readOnly = true)
    public List<OrderItemDto> getOrderItemsByOrderId(Integer orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow();
        return order.getOrderItems().stream()
                .map(orderItemMapper::toDto)
                .collect(Collectors.toList());
    }
}

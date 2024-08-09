package com.example.OrderService.service;

import com.example.OrderService.annotation.RequestLogger;
import com.example.OrderService.client.ProductDto;
import com.example.OrderService.client.ProductServiceClient;
import com.example.OrderService.dto.OrderDto;
import com.example.OrderService.dto.OrderItemDto;
import com.example.OrderService.entity.Order;
import com.example.OrderService.entity.OrderItem;
import com.example.OrderService.exception.RecordNotFoundException;
import com.example.OrderService.mapper.OrderItemMapper;
import com.example.OrderService.mapper.OrderMapper;
import com.example.OrderService.repository.OrderRepository;
import com.example.OrderService.repository.OrderItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
@RequestLogger
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;
    private final ProductServiceClient productServiceClient;

    private final OrderService self;

    @Cacheable("order")
    public Optional<OrderDto> getOrderById(Integer id) {
        log.info("Getting order by id: {}", id);
        return orderRepository.findById(id)
                .map(orderMapper::toDto)
                .or(() -> {
                    log.error("Order not found with id {}", id);
                    throw new RecordNotFoundException("Order not found with id " + id);
                });
    }

    @CachePut(value = "order", key = "#result.id")
    public OrderDto createOrder(OrderDto orderDto) {
        log.info("Creating order: {}", orderDto);
        Order order = orderMapper.toEntity(orderDto);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus(1);
        order.setTotalAmount(0.0);
        order = orderRepository.save(order);
        return orderMapper.toDto(order);
    }

    @CachePut(value = "order", key = "#orderDto.id")
    public OrderDto updateOrder(Integer id, OrderDto orderDto) {
        log.info("Updating order with id: {}", id);
        Order order = orderMapper.toEntity(orderDto);
        order.setId(id);
        order = orderRepository.save(order);
        return orderMapper.toDto(order);
    }

    @CacheEvict(value = "order", key = "#id")
    public void deleteOrder(Integer id) {
        log.info("Deleting order with id: {}", id);
        orderRepository.deleteById(id);
    }

    @Transactional
    public OrderItemDto addOrderItem(Integer orderId, OrderItemDto orderItemDto) {
        log.info("Adding order item to order with id: {}", orderId);
        Order order = findOrderById(orderId);
        OrderItem orderItem = findOrCreateOrderItem(order, orderItemDto);
        updateOrderTotalAmount(order, orderItem.getTotalAmount());

        orderItem = orderItemRepository.save(orderItem);
        orderRepository.save(order);

        return orderItemMapper.toDto(orderItem);
    }

    @Transactional
    public OrderItemDto updateOrderItem(Integer orderId, Integer orderItemId, OrderItemDto orderItemDto) {
        log.info("Updating order item with id: {}", orderItemId);
        Order order = findOrderById(orderId);
        OrderItem orderItem = findOrderItemById(orderItemId);

        validateOrderItemBelongsToOrder(order, orderItem);
        handleQuantityZero(order, orderItemDto, orderItemId);

        double oldTotalAmount = Optional.ofNullable(orderItem.getTotalAmount()).orElse(0.0);
        updateOrderItemDetails(orderItem, orderItemDto);

        updateOrderTotalAmount(order, orderItem.getTotalAmount() - oldTotalAmount);
        orderItem = orderItemRepository.save(orderItem);
        orderRepository.save(order);

        return orderItemMapper.toDto(orderItem);
    }

    @Transactional
    public void deleteOrderItem(Integer orderId, Integer orderItemId) {
        log.info("Deleting order item with id: {}", orderItemId);
        Order order = findOrderById(orderId);
        OrderItem orderItem = findOrderItemById(orderItemId);

        validateOrderItemBelongsToOrder(order, orderItem);
        updateOrderTotalAmount(order, -Optional.ofNullable(orderItem.getTotalAmount()).orElse(0.0));

        order.getOrderItems().remove(orderItem);
        orderItemRepository.delete(orderItem);
        orderRepository.save(order);
    }

    @Transactional(readOnly = true)
    public List<OrderItemDto> getOrderItemsByOrderId(Integer orderId) {
        log.info("Getting order items for order with id: {}", orderId);
        Order order = findOrderById(orderId);
        return order.getOrderItems().stream()
                .map(orderItemMapper::toDto)
                .collect(Collectors.toList());
    }

    private Order findOrderById(Integer orderId) {
        log.info("Finding order by id: {}", orderId);
        return orderRepository.findById(orderId)
                .orElseThrow(() -> {
                    log.error("Order not found with id {}", orderId);
                    return new RecordNotFoundException("Order not found with id " + orderId);
                });
    }

    private OrderItem findOrderItemById(Integer orderItemId) {
        log.info("Finding order item by id: {}", orderItemId);
        return orderItemRepository.findById(orderItemId)
                .orElseThrow(() -> {
                    log.error("Order item not found with id {}", orderItemId);
                    return new RecordNotFoundException("Order item not found with id " + orderItemId);
                });
    }

    private OrderItem findOrCreateOrderItem(Order order, OrderItemDto orderItemDto) {
        return order.getOrderItems().stream()
                .filter(item -> item.getProductId().equals(orderItemDto.getProductId()))
                .findFirst()
                .map(existingOrderItem -> {
                    updateOrderTotalAmount(order, -Optional.ofNullable(existingOrderItem.getTotalAmount()).orElse(0.0));
                    existingOrderItem.setQuantity(existingOrderItem.getQuantity() + orderItemDto.getQuantity());
                    updateOrderItemTotalAmount(existingOrderItem);
                    return existingOrderItem;
                })
                .orElseGet(() -> createNewOrderItem(order, orderItemDto));
    }

    private OrderItem createNewOrderItem(Order order, OrderItemDto orderItemDto) {
        OrderItem orderItem = orderItemMapper.toEntity(orderItemDto);
        updateOrderItemTotalAmount(orderItem);
        orderItem.setOrder(order);
        order.getOrderItems().add(orderItem);
        return orderItem;
    }

    private void updateOrderItemTotalAmount(OrderItem orderItem) {
        ProductDto productDto = productServiceClient.getProductById(orderItem.getProductId());
        orderItem.setPrice(productDto.getPrice());
        orderItem.setTotalAmount(productDto.getPrice() * orderItem.getQuantity());
    }

    private void updateOrderTotalAmount(Order order, double amount) {
        order.setTotalAmount(Optional.ofNullable(order.getTotalAmount()).orElse(0.0) + amount);
    }

    private void updateOrderItemDetails(OrderItem orderItem, OrderItemDto orderItemDto) {
        orderItem.setQuantity(orderItemDto.getQuantity());
        updateOrderItemTotalAmount(orderItem);
    }

    private void validateOrderItemBelongsToOrder(Order order, OrderItem orderItem) {
        if (!order.getOrderItems().contains(orderItem)) {
            throw new IllegalArgumentException("Order item does not belong to the specified order.");
        }
    }

    private void handleQuantityZero(Order order, OrderItemDto orderItemDto, Integer orderItemId) {
        if (orderItemDto.getQuantity() <= 0) {
            self.deleteOrderItem(order.getId(), orderItemId);
        }
    }

}

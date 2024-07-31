package com.example.orders.service;

import com.example.orders.dto.OrderDto;
import com.example.orders.entity.Order;
import com.example.orders.mapper.OrderMapper;
import com.example.orders.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderService orderService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetOrderById_Success() {
        // Given
        Order order = new Order();
        order.setId(1);
        OrderDto orderDto = OrderMapper.INSTANCE.toDto(order);
        when(orderRepository.findById(1)).thenReturn(Optional.of(order));

        // When
        OrderDto result = orderService.getOrderById(1);

        // Then
        verify(orderRepository).findById(1);
        assertEquals(orderDto.getId(), result.getId());
    }

    @Test
    public void testGetOrderById_NotFound() {
        // Given
        when(orderRepository.findById(1)).thenReturn(Optional.empty());

        // When & Then
        RuntimeException thrown = assertThrows(RuntimeException.class, () -> orderService.getOrderById(1));
        assertEquals("Order not found", thrown.getMessage());
    }

    @Test
    @Transactional
    public void testCreateOrder() {
        // Given
        OrderDto orderDto = new OrderDto();
        orderDto.setId(1);
        orderDto.setOrderDate(LocalDateTime.now());
        orderDto.setStatus("PENDING");
        orderDto.setTotalAmount(100.0);
        orderDto.setCustomerId(1);
        Order order = OrderMapper.INSTANCE.toEntity(orderDto);
        when(orderRepository.save(order)).thenReturn(order);

        // When
        OrderDto result = orderService.createOrder(orderDto);

        // Then
        verify(orderRepository).save(order);
        assertEquals(orderDto.getId(), result.getId());
        assertEquals(orderDto.getStatus(), result.getStatus());
    }

    @Test
    @Transactional
    public void testUpdateOrder() {
        // Given
        OrderDto orderDto = new OrderDto();
        orderDto.setId(1);
        orderDto.setOrderDate(LocalDateTime.now());
        orderDto.setStatus("SHIPPED");
        orderDto.setTotalAmount(150.0);
        orderDto.setCustomerId(1);
        Order order = new Order();
        order.setId(1);
        when(orderRepository.findById(1)).thenReturn(Optional.of(order));
        when(orderRepository.save(order)).thenReturn(order);

        // When
        OrderDto result = orderService.updateOrder(orderDto);

        // Then
        verify(orderRepository).findById(1);
        verify(orderRepository).save(order);
        assertEquals(orderDto.getStatus(), result.getStatus());
        assertEquals(orderDto.getTotalAmount(), result.getTotalAmount());
    }

    @Test
    @Transactional
    public void testDeleteOrder() {
        // When
        orderService.deleteOrder(1);

        // Then
        verify(orderRepository).deleteById(1);
    }
}

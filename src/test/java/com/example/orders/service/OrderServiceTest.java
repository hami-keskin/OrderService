package com.example.orders.service;

import com.example.orders.dto.OrderDto;
import com.example.orders.entity.Order;
import com.example.orders.repository.OrderRepository;
import com.example.orders.mapper.OrderMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderMapper orderMapper;

    @InjectMocks
    private OrderService orderService;

    private OrderDto orderDto;
    private Order order;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        orderDto = TestData.createOrderDto();
        order = TestData.createOrder();
        when(orderMapper.toDto(order)).thenReturn(orderDto);
        when(orderMapper.toEntity(orderDto)).thenReturn(order);
    }

    @Test
    public void testGetOrderById_Success() {
        // Given
        when(orderRepository.findById(1)).thenReturn(Optional.of(order));

        // When
        var result = orderService.getOrderById(1);

        // Then
        assertEquals(orderDto, result);
        verify(orderRepository).findById(1);
    }

    @Test
    public void testGetOrderById_NotFound() {
        // Given
        when(orderRepository.findById(1)).thenReturn(Optional.empty());

        // When & Then
        var exception = assertThrows(RuntimeException.class, () -> orderService.getOrderById(1));
        assertEquals("Order not found", exception.getMessage());
    }

    @Test
    public void testCreateOrder() {
        // Given
        when(orderRepository.save(any(Order.class))).thenReturn(order);

        // When
        var result = orderService.createOrder(orderDto);

        // Then
        assertEquals(orderDto, result);
        verify(orderRepository).save(any(Order.class));
    }

    @Test
    public void testUpdateOrder() {
        // Given
        when(orderRepository.findById(1)).thenReturn(Optional.of(order));
        when(orderRepository.save(any(Order.class))).thenReturn(order);

        // When
        var result = orderService.updateOrder(orderDto);

        // Then
        assertEquals(orderDto, result);
        verify(orderRepository).findById(1);
        verify(orderRepository).save(any(Order.class));
    }

    @Test
    public void testDeleteOrder() {
        // When
        orderService.deleteOrder(1);

        // Then
        verify(orderRepository).deleteById(1);
    }
}

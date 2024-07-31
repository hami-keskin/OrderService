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

    private OrderDto createOrderDto() {
        return TestData.createOrderDto();
    }

    private Order createOrder() {
        return TestData.createOrder();
    }

    @Test
    public void testGetOrderById_Success() {
        var order = createOrder();
        var orderDto = createOrderDto();
        when(orderRepository.findById(1)).thenReturn(Optional.of(order));

        var result = orderService.getOrderById(1);

        verify(orderRepository).findById(1);
        assertEquals(orderDto, result);
    }

    @Test
    public void testGetOrderById_NotFound() {
        when(orderRepository.findById(1)).thenReturn(Optional.empty());

        var exception = assertThrows(RuntimeException.class, () -> orderService.getOrderById(1));
        assertEquals("Order not found", exception.getMessage());
    }

    @Test
    public void testCreateOrder() {
        var orderDto = createOrderDto();
        var order = createOrder();
        when(orderRepository.save(any(Order.class))).thenReturn(order);

        var result = orderService.createOrder(orderDto);

        verify(orderRepository).save(any(Order.class));
        assertEquals(orderDto, result);
    }

    @Test
    public void testUpdateOrder() {
        var orderDto = createOrderDto();
        var order = createOrder();
        when(orderRepository.findById(1)).thenReturn(Optional.of(order));
        when(orderRepository.save(any(Order.class))).thenReturn(order);

        var result = orderService.updateOrder(orderDto);

        verify(orderRepository).findById(1);
        verify(orderRepository).save(any(Order.class));
        assertEquals(orderDto, result);
    }

    @Test
    public void testDeleteOrder() {
        orderService.deleteOrder(1);
        verify(orderRepository).deleteById(1);
    }
}

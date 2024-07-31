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

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrderServiceTest {

    @InjectMocks
    private OrderService orderService;

    @Mock
    private OrderRepository orderRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetOrderById() {
        Order order = new Order();
        order.setId(1);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus("NEW");
        order.setTotalAmount(100.0);
        order.setCustomerId(1);

        when(orderRepository.findById(1)).thenReturn(Optional.of(order));

        OrderDto orderDto = orderService.getOrderById(1);
        assertNotNull(orderDto);
        assertEquals(1, orderDto.getId());
        assertEquals("NEW", orderDto.getStatus());
        assertEquals(100.0, orderDto.getTotalAmount());
        verify(orderRepository, times(1)).findById(1);
    }

    @Test
    void testCreateOrder() {
        OrderDto orderDto = new OrderDto();
        orderDto.setOrderDate(LocalDateTime.now());
        orderDto.setStatus("NEW");
        orderDto.setTotalAmount(100.0);
        orderDto.setCustomerId(1);

        Order order = new Order();
        order.setOrderDate(orderDto.getOrderDate());
        order.setStatus(orderDto.getStatus());
        order.setTotalAmount(orderDto.getTotalAmount());
        order.setCustomerId(orderDto.getCustomerId());

        when(orderRepository.save(any(Order.class))).thenReturn(order);

        OrderDto createdOrderDto = orderService.createOrder(orderDto);
        assertNotNull(createdOrderDto);
        assertEquals("NEW", createdOrderDto.getStatus());
        assertEquals(100.0, createdOrderDto.getTotalAmount());
        verify(orderRepository, times(1)).save(any(Order.class));
    }

    @Test
    void testUpdateOrder() {
        OrderDto orderDto = new OrderDto();
        orderDto.setId(1);
        orderDto.setOrderDate(LocalDateTime.now());
        orderDto.setStatus("UPDATED");
        orderDto.setTotalAmount(150.0);
        orderDto.setCustomerId(2);

        Order existingOrder = new Order();
        existingOrder.setId(1);
        existingOrder.setStatus("NEW");
        existingOrder.setTotalAmount(100.0);

        when(orderRepository.findById(1)).thenReturn(Optional.of(existingOrder));
        when(orderRepository.save(any(Order.class))).thenReturn(existingOrder);

        OrderDto updatedOrderDto = orderService.updateOrder(orderDto);
        assertNotNull(updatedOrderDto);
        assertEquals("UPDATED", updatedOrderDto.getStatus());
        assertEquals(150.0, updatedOrderDto.getTotalAmount());
        verify(orderRepository, times(1)).findById(1);
        verify(orderRepository, times(1)).save(any(Order.class));
    }

    @Test
    void testDeleteOrder() {
        doNothing().when(orderRepository).deleteById(1);

        orderService.deleteOrder(1);
        verify(orderRepository, times(1)).deleteById(1);
    }
}

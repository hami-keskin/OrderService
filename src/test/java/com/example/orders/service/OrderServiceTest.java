package com.example.orders.service;

import com.example.orders.dto.OrderDto;
import com.example.orders.entity.Order;
import com.example.orders.mapper.OrderMapper;
import com.example.orders.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    private OrderService orderService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        orderService = new OrderService(orderRepository);
    }

    @Test
    public void testGetOrderById() {
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
    }

    @Test
    public void testCreateOrder() {
        OrderDto orderDto = new OrderDto();
        orderDto.setOrderDate(LocalDateTime.now());
        orderDto.setStatus("NEW");
        orderDto.setTotalAmount(100.0);
        orderDto.setCustomerId(1);

        Order order = OrderMapper.INSTANCE.toEntity(orderDto);
        when(orderRepository.save(any(Order.class))).thenReturn(order);

        OrderDto createdOrder = orderService.createOrder(orderDto);
        assertNotNull(createdOrder);
        assertEquals("NEW", createdOrder.getStatus());
    }

    @Test
    public void testUpdateOrder() {
        OrderDto orderDto = new OrderDto();
        orderDto.setId(1);
        orderDto.setOrderDate(LocalDateTime.now());
        orderDto.setStatus("UPDATED");
        orderDto.setTotalAmount(200.0);
        orderDto.setCustomerId(1);

        Order order = new Order();
        order.setId(1);
        when(orderRepository.findById(1)).thenReturn(Optional.of(order));
        when(orderRepository.save(any(Order.class))).thenReturn(order);

        OrderDto updatedOrder = orderService.updateOrder(orderDto);
        assertNotNull(updatedOrder);
        assertEquals("UPDATED", updatedOrder.getStatus());
    }

    @Test
    public void testDeleteOrder() {
        doNothing().when(orderRepository).deleteById(1);
        orderService.deleteOrder(1);
        verify(orderRepository, times(1)).deleteById(1);
    }
}

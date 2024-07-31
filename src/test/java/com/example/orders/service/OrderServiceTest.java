package com.example.orders.service;

import com.example.orders.dto.OrderDto;
import com.example.orders.entity.Order;
import com.example.orders.mapper.OrderMapper;
import com.example.orders.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
@SpringBootTest
public class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderService orderService;

    private Order order;
    private OrderDto orderDto;

    @BeforeEach
    void setUp() {
        order = new Order();
        order.setId(1);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus("NEW");
        order.setTotalAmount(100.0);
        order.setCustomerId(1);

        orderDto = new OrderDto();
        orderDto.setId(1);
        orderDto.setOrderDate(LocalDateTime.now());
        orderDto.setStatus("NEW");
        orderDto.setTotalAmount(100.0);
        orderDto.setCustomerId(1);
    }

    @Test
    public void testGetOrderById() {
        when(orderRepository.findById(1)).thenReturn(Optional.of(order));
        OrderDto foundOrder = orderService.getOrderById(1);
        assertNotNull(foundOrder);
        assertEquals(1, foundOrder.getId());
    }

    @Test
    public void testCreateOrder() {
        when(orderRepository.save(any(Order.class))).thenReturn(order);
        OrderDto savedOrderDto = orderService.createOrder(orderDto);
        assertNotNull(savedOrderDto);
        assertEquals(order.getId(), savedOrderDto.getId());
    }

    @Test
    public void testUpdateOrder() {
        when(orderRepository.findById(1)).thenReturn(Optional.of(order));
        when(orderRepository.save(any(Order.class))).thenReturn(order);
        OrderDto updatedOrderDto = orderService.updateOrder(orderDto);
        assertNotNull(updatedOrderDto);
        assertEquals(order.getId(), updatedOrderDto.getId());
    }

    @Test
    public void testDeleteOrder() {
        doNothing().when(orderRepository).deleteById(1);
        orderService.deleteOrder(1);
        verify(orderRepository, times(1)).deleteById(1);
    }

    @Test
    public void testGetOrderById_NotFound() {
        when(orderRepository.findById(1)).thenReturn(Optional.empty());
        OrderDto foundOrder = orderService.getOrderById(1);
        assertNull(foundOrder);
    }

    @Test
    public void testCreateOrder_InvalidData() {
        orderDto.setTotalAmount(-100.0);
        assertThrows(IllegalArgumentException.class, () -> orderService.createOrder(orderDto));
    }

    @Test
    public void testUpdateOrder_NotFound() {
        when(orderRepository.findById(1)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> orderService.updateOrder(orderDto));
    }

    @Test
    public void testDeleteOrder_NotFound() {
        doThrow(new RuntimeException("Order not found")).when(orderRepository).deleteById(1);
        assertThrows(RuntimeException.class, () -> orderService.deleteOrder(1));
    }
}

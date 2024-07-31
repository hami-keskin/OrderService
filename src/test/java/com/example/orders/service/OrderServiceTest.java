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
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderService orderService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetOrderById() {
        Order order = TestData.createOrder(1, LocalDateTime.now(), "Processing", 100.0, 1, null);
        when(orderRepository.findById(1)).thenReturn(Optional.of(order));

        OrderDto orderDto = orderService.getOrderById(1);

        assertNotNull(orderDto);
        assertEquals(1, orderDto.getId());
        verify(orderRepository, times(1)).findById(1);
    }

    @Test
    void testCreateOrder() {
        OrderDto orderDto = new OrderDto();
        orderDto.setOrderDate(LocalDateTime.now());
        orderDto.setStatus("Processing");
        orderDto.setTotalAmount(100.0);
        orderDto.setCustomerId(1);

        Order order = OrderMapper.INSTANCE.toEntity(orderDto);
        when(orderRepository.save(any(Order.class))).thenReturn(order);

        OrderDto createdOrder = orderService.createOrder(orderDto);

        assertNotNull(createdOrder);
        assertEquals(100.0, createdOrder.getTotalAmount());
        verify(orderRepository, times(1)).save(any(Order.class));
    }

    @Test
    void testUpdateOrder() {
        Order order = TestData.createOrder(1, LocalDateTime.now(), "Processing", 100.0, 1, null);
        when(orderRepository.findById(1)).thenReturn(Optional.of(order));

        OrderDto orderDto = new OrderDto();
        orderDto.setId(1);
        orderDto.setOrderDate(LocalDateTime.now().plusDays(1));
        orderDto.setStatus("Shipped");
        orderDto.setTotalAmount(150.0);
        orderDto.setCustomerId(1);

        when(orderRepository.save(any(Order.class))).thenReturn(order);

        OrderDto updatedOrder = orderService.updateOrder(orderDto);

        assertNotNull(updatedOrder);
        assertEquals("Shipped", updatedOrder.getStatus());
        assertEquals(150.0, updatedOrder.getTotalAmount());
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

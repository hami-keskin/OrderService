package com.example.orders.service;

import com.example.orders.dto.OrderDto;
import com.example.orders.entity.Order;
import com.example.orders.mapper.OrderMapper;
import com.example.orders.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderMapper orderMapper;

    private OrderService orderService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        orderService = new OrderService(orderRepository, orderMapper);
    }

    @Test
    void testGetOrderById() {
        Order order = new Order();
        OrderDto orderDto = new OrderDto();
        when(orderRepository.findById(1)).thenReturn(Optional.of(order));
        when(orderMapper.toDto(order)).thenReturn(orderDto);

        Optional<OrderDto> result = orderService.getOrderById(1);
        assertTrue(result.isPresent());
        assertEquals(orderDto, result.get());
    }

    @Test
    void testCreateOrder() {
        Order order = new Order();
        OrderDto orderDto = new OrderDto();
        orderDto.setOrderDate(LocalDateTime.now());
        orderDto.setStatus("NEW");
        orderDto.setTotalAmount(100.0);
        orderDto.setCustomerId(1);

        when(orderMapper.toEntity(orderDto)).thenReturn(order);
        when(orderRepository.save(order)).thenReturn(order);
        when(orderMapper.toDto(order)).thenReturn(orderDto);

        OrderDto result = orderService.createOrder(orderDto);
        assertEquals(orderDto, result);
    }

    @Test
    void testUpdateOrder() {
        Order order = new Order();
        OrderDto orderDto = new OrderDto();
        orderDto.setId(1);
        orderDto.setOrderDate(LocalDateTime.now());
        orderDto.setStatus("UPDATED");
        orderDto.setTotalAmount(200.0);
        orderDto.setCustomerId(1);

        when(orderMapper.toEntity(orderDto)).thenReturn(order);
        when(orderRepository.save(order)).thenReturn(order);
        when(orderMapper.toDto(order)).thenReturn(orderDto);

        OrderDto result = orderService.updateOrder(orderDto);
        assertEquals(orderDto, result);
    }

    @Test
    void testDeleteOrder() {
        doNothing().when(orderRepository).deleteById(1);
        orderService.deleteOrder(1);
        verify(orderRepository, times(1)).deleteById(1);
    }
}

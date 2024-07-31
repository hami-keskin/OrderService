package com.example.orders.service;

import com.example.orders.client.ProductClient;
import com.example.orders.dto.OrderItemDto;
import com.example.orders.entity.Order;
import com.example.orders.entity.OrderItem;
import com.example.orders.mapper.OrderItemMapper;
import com.example.orders.repository.OrderItemRepository;
import com.example.orders.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class OrderItemServiceTest {

    @Mock
    private OrderItemRepository orderItemRepository;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ProductClient productClient;

    @InjectMocks
    private OrderItemService orderItemService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetOrderItemById() {
        OrderItem orderItem = TestData.createOrderItem(1, 101, 2, 50.0, null);
        when(orderItemRepository.findById(1)).thenReturn(Optional.of(orderItem));

        OrderItemDto orderItemDto = orderItemService.getOrderItemById(1);

        assertNotNull(orderItemDto);
        assertEquals(101, orderItemDto.getProductId());
        verify(orderItemRepository, times(1)).findById(1);
    }

    @Test
    void testCreateOrderItem() {
        OrderItemDto orderItemDto = new OrderItemDto();
        orderItemDto.setProductId(101);
        orderItemDto.setQuantity(2);
        orderItemDto.setOrderId(1);

        Order order = TestData.createOrder(1, LocalDateTime.now(), "Processing", 100.0, 1, null);
        when(orderRepository.findById(1)).thenReturn(Optional.of(order));

        Map<String, Object> product = Map.of("price", 50.0);
        when(productClient.getProductById(101)).thenReturn(product);

        OrderItem orderItem = OrderItemMapper.INSTANCE.toEntity(orderItemDto);
        orderItem.setPrice(50.0);
        when(orderItemRepository.save(any(OrderItem.class))).thenReturn(orderItem);

        OrderItemDto createdOrderItem = orderItemService.createOrderItem(orderItemDto);

        assertNotNull(createdOrderItem);
        assertEquals(50.0, createdOrderItem.getPrice());
        verify(orderRepository, times(1)).findById(1);
        verify(productClient, times(1)).getProductById(101);
        verify(orderItemRepository, times(1)).save(any(OrderItem.class));
    }

    @Test
    void testUpdateOrderItem() {
        OrderItem orderItem = TestData.createOrderItem(1, 101, 2, 50.0, null);
        when(orderItemRepository.findById(1)).thenReturn(Optional.of(orderItem));

        Order order = TestData.createOrder(1, LocalDateTime.now(), "Processing", 100.0, 1, null);
        when(orderRepository.findById(1)).thenReturn(Optional.of(order));

        Map<String, Object> product = Map.of("price", 60.0);
        when(productClient.getProductById(101)).thenReturn(product);

        OrderItemDto orderItemDto = new OrderItemDto();
        orderItemDto.setId(1);
        orderItemDto.setProductId(101);
        orderItemDto.setQuantity(3);
        orderItemDto.setOrderId(1);

        OrderItem updatedOrderItem = OrderItemMapper.INSTANCE.toEntity(orderItemDto);
        updatedOrderItem.setPrice(60.0);
        when(orderItemRepository.save(any(OrderItem.class))).thenReturn(updatedOrderItem);

        OrderItemDto updatedOrderItemDto = orderItemService.updateOrderItem(orderItemDto);

        assertNotNull(updatedOrderItemDto);
        assertEquals(60.0, updatedOrderItemDto.getPrice());
        assertEquals(3, updatedOrderItemDto.getQuantity());
        verify(orderItemRepository, times(1)).findById(1);
        verify(orderRepository, times(1)).findById(1);
        verify(productClient, times(1)).getProductById(101);
        verify(orderItemRepository, times(1)).save(any(OrderItem.class));
    }

    @Test
    void testDeleteOrderItem() {
        doNothing().when(orderItemRepository).deleteById(1);

        orderItemService.deleteOrderItem(1);

        verify(orderItemRepository, times(1)).deleteById(1);
    }
}

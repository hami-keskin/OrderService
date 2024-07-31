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
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetOrderItemById_Success() {
        // Given
        OrderItem orderItem = TestData.createOrderItem();
        OrderItemDto orderItemDto = TestData.createOrderItemDto();
        when(orderItemRepository.findById(1)).thenReturn(Optional.of(orderItem));

        // When
        OrderItemDto result = orderItemService.getOrderItemById(1);

        // Then
        verify(orderItemRepository).findById(1);
        assertEquals(orderItemDto.getId(), result.getId());
        assertEquals(orderItemDto.getProductId(), result.getProductId());
        assertEquals(orderItemDto.getQuantity(), result.getQuantity());
    }

    @Test
    public void testGetOrderItemById_NotFound() {
        // Given
        when(orderItemRepository.findById(1)).thenReturn(Optional.empty());

        // When & Then
        RuntimeException thrown = assertThrows(RuntimeException.class, () -> orderItemService.getOrderItemById(1));
        assertEquals("Order item not found", thrown.getMessage());
    }

    @Test
    @Transactional
    public void testCreateOrderItem() {
        // Given
        OrderItemDto orderItemDto = TestData.createOrderItemDto();
        OrderItem orderItem = TestData.createOrderItem();
        Map<String, Object> product = TestData.createProduct();
        Order order = TestData.createOrder();
        when(productClient.getProductById(1)).thenReturn(product);
        when(orderRepository.findById(1)).thenReturn(Optional.of(order));
        when(orderItemRepository.save(any(OrderItem.class))).thenReturn(orderItem);

        // When
        OrderItemDto result = orderItemService.createOrderItem(orderItemDto);

        // Then
        verify(productClient).getProductById(1);
        verify(orderRepository).findById(1);
        verify(orderItemRepository).save(any(OrderItem.class));
        assertEquals(orderItemDto.getQuantity(), result.getQuantity());
        assertEquals(50.0, result.getPrice());
    }

    @Test
    @Transactional
    public void testUpdateOrderItem() {
        // Given
        OrderItemDto orderItemDto = TestData.createOrderItemDto();
        OrderItem orderItem = TestData.createOrderItem();
        Map<String, Object> product = TestData.createProduct();
        Order order = TestData.createOrder();
        when(orderItemRepository.findById(1)).thenReturn(Optional.of(orderItem));
        when(orderRepository.findById(1)).thenReturn(Optional.of(order));
        when(productClient.getProductById(1)).thenReturn(product);
        when(orderItemRepository.save(any(OrderItem.class))).thenReturn(orderItem);

        // When
        OrderItemDto result = orderItemService.updateOrderItem(orderItemDto);

        // Then
        verify(orderItemRepository).findById(1);
        verify(orderRepository).findById(1);
        verify(productClient).getProductById(1);
        verify(orderItemRepository).save(any(OrderItem.class));
        assertEquals(orderItemDto.getQuantity(), result.getQuantity());
        assertEquals(50.0, result.getPrice());
    }

    @Test
    @Transactional
    public void testDeleteOrderItem() {
        // When
        orderItemService.deleteOrderItem(1);

        // Then
        verify(orderItemRepository).deleteById(1);
    }

    @Test
    public void testCreateOrderItem_ProductNotFound() {
        // Given
        OrderItemDto orderItemDto = TestData.createOrderItemDto();
        when(productClient.getProductById(1)).thenReturn(null);

        // When & Then
        assertThrows(RuntimeException.class, () -> orderItemService.createOrderItem(orderItemDto));
    }

    @Test
    public void testUpdateOrderItem_OrderNotFound() {
        // Given
        OrderItemDto orderItemDto = TestData.createOrderItemDto();
        when(orderItemRepository.findById(1)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(RuntimeException.class, () -> orderItemService.updateOrderItem(orderItemDto));
    }
}

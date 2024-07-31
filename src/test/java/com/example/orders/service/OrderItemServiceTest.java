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

    private OrderItemDto orderItemDto;
    private OrderItem orderItem;
    private Order order;
    private Map<String, Object> product;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        orderItemDto = TestData.createOrderItemDto();
        orderItem = TestData.createOrderItem();
        order = TestData.createOrder();
        product = TestData.createProduct();
    }

    @Test
    public void testGetOrderItemById_Success() {
        // Given
        when(orderItemRepository.findById(1)).thenReturn(Optional.of(orderItem));

        // When
        var result = orderItemService.getOrderItemById(1);

        // Then
        assertEquals(orderItemDto, result);
        verify(orderItemRepository).findById(1);
    }

    @Test
    public void testGetOrderItemById_NotFound() {
        // Given
        when(orderItemRepository.findById(1)).thenReturn(Optional.empty());

        // When & Then
        var exception = assertThrows(RuntimeException.class, () -> orderItemService.getOrderItemById(1));
        assertEquals("Order item not found", exception.getMessage());
    }

    @Test
    public void testCreateOrderItem() {
        // Given
        when(productClient.getProductById(1)).thenReturn(product);
        when(orderRepository.findById(1)).thenReturn(Optional.of(order));
        when(orderItemRepository.save(any(OrderItem.class))).thenReturn(orderItem);

        // When
        var result = orderItemService.createOrderItem(orderItemDto);

        // Then
        assertEquals(orderItemDto.getQuantity(), result.getQuantity());
        assertEquals(50.0, result.getPrice());
        verify(productClient).getProductById(1);
        verify(orderRepository).findById(1);
        verify(orderItemRepository).save(any(OrderItem.class));
    }

    @Test
    public void testUpdateOrderItem() {
        // Given
        when(orderItemRepository.findById(1)).thenReturn(Optional.of(orderItem));
        when(orderRepository.findById(1)).thenReturn(Optional.of(order));
        when(productClient.getProductById(1)).thenReturn(product);
        when(orderItemRepository.save(any(OrderItem.class))).thenReturn(orderItem);

        // When
        var result = orderItemService.updateOrderItem(orderItemDto);

        // Then
        assertEquals(orderItemDto.getQuantity(), result.getQuantity());
        assertEquals(50.0, result.getPrice());
        verify(orderItemRepository).findById(1);
        verify(orderRepository).findById(1);
        verify(productClient).getProductById(1);
        verify(orderItemRepository).save(any(OrderItem.class));
    }

    @Test
    public void testDeleteOrderItem() {
        // When
        orderItemService.deleteOrderItem(1);

        // Then
        verify(orderItemRepository).deleteById(1);
    }

    @Test
    public void testCreateOrderItem_ProductNotFound() {
        // Given
        when(productClient.getProductById(1)).thenReturn(null);

        // When & Then
        assertThrows(RuntimeException.class, () -> orderItemService.createOrderItem(orderItemDto));
    }

    @Test
    public void testUpdateOrderItem_OrderNotFound() {
        // Given
        when(orderItemRepository.findById(1)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(RuntimeException.class, () -> orderItemService.updateOrderItem(orderItemDto));
    }
}

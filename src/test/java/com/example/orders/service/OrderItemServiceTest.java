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

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private OrderItemDto createOrderItemDto() {
        return TestData.createOrderItemDto();
    }

    private OrderItem createOrderItem() {
        return TestData.createOrderItem();
    }

    private Order createOrder() {
        return TestData.createOrder();
    }

    private Map<String, Object> createProduct() {
        return TestData.createProduct();
    }

    @Test
    public void testGetOrderItemById_Success() {
        var orderItem = createOrderItem();
        var orderItemDto = createOrderItemDto();
        when(orderItemRepository.findById(1)).thenReturn(Optional.of(orderItem));

        var result = orderItemService.getOrderItemById(1);

        verify(orderItemRepository).findById(1);
        assertEquals(orderItemDto, result);
    }

    @Test
    public void testGetOrderItemById_NotFound() {
        when(orderItemRepository.findById(1)).thenReturn(Optional.empty());

        var exception = assertThrows(RuntimeException.class, () -> orderItemService.getOrderItemById(1));
        assertEquals("Order item not found", exception.getMessage());
    }

    @Test
    public void testCreateOrderItem() {
        var orderItemDto = createOrderItemDto();
        var orderItem = createOrderItem();
        var product = createProduct();
        var order = createOrder();

        when(productClient.getProductById(1)).thenReturn(product);
        when(orderRepository.findById(1)).thenReturn(Optional.of(order));
        when(orderItemRepository.save(any(OrderItem.class))).thenReturn(orderItem);

        var result = orderItemService.createOrderItem(orderItemDto);

        verify(productClient).getProductById(1);
        verify(orderRepository).findById(1);
        verify(orderItemRepository).save(any(OrderItem.class));
        assertEquals(orderItemDto.getQuantity(), result.getQuantity());
        assertEquals(50.0, result.getPrice());
    }

    @Test
    public void testUpdateOrderItem() {
        var orderItemDto = createOrderItemDto();
        var orderItem = createOrderItem();
        var product = createProduct();
        var order = createOrder();

        when(orderItemRepository.findById(1)).thenReturn(Optional.of(orderItem));
        when(orderRepository.findById(1)).thenReturn(Optional.of(order));
        when(productClient.getProductById(1)).thenReturn(product);
        when(orderItemRepository.save(any(OrderItem.class))).thenReturn(orderItem);

        var result = orderItemService.updateOrderItem(orderItemDto);

        verify(orderItemRepository).findById(1);
        verify(orderRepository).findById(1);
        verify(productClient).getProductById(1);
        verify(orderItemRepository).save(any(OrderItem.class));
        assertEquals(orderItemDto.getQuantity(), result.getQuantity());
        assertEquals(50.0, result.getPrice());
    }

    @Test
    public void testDeleteOrderItem() {
        orderItemService.deleteOrderItem(1);
        verify(orderItemRepository).deleteById(1);
    }

    @Test
    public void testCreateOrderItem_ProductNotFound() {
        var orderItemDto = createOrderItemDto();
        when(productClient.getProductById(1)).thenReturn(null);

        assertThrows(RuntimeException.class, () -> orderItemService.createOrderItem(orderItemDto));
    }

    @Test
    public void testUpdateOrderItem_OrderNotFound() {
        var orderItemDto = createOrderItemDto();
        when(orderItemRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> orderItemService.updateOrderItem(orderItemDto));
    }
}

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

class OrderItemServiceTest {

    @InjectMocks
    private OrderItemService orderItemService;

    @Mock
    private OrderItemRepository orderItemRepository;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ProductClient productClient;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetOrderItemById() {
        OrderItem orderItem = new OrderItem();
        orderItem.setId(1);
        orderItem.setProductId(10);
        orderItem.setQuantity(5);
        orderItem.setPrice(100.0);

        when(orderItemRepository.findById(1)).thenReturn(Optional.of(orderItem));

        OrderItemDto orderItemDto = orderItemService.getOrderItemById(1);
        assertNotNull(orderItemDto);
        assertEquals(1, orderItemDto.getId());
        assertEquals(10, orderItemDto.getProductId());
        assertEquals(100.0, orderItemDto.getPrice());
        verify(orderItemRepository, times(1)).findById(1);
    }

    @Test
    void testCreateOrderItem() {
        OrderItemDto orderItemDto = new OrderItemDto();
        orderItemDto.setProductId(10);
        orderItemDto.setQuantity(5);
        orderItemDto.setOrderId(1);

        OrderItem orderItem = new OrderItem();
        orderItem.setProductId(10);
        orderItem.setQuantity(5);

        Order order = new Order();
        order.setId(1);

        Map<String, Object> product = Map.of("price", 100.0);

        when(productClient.getProductById(10)).thenReturn(product);
        when(orderRepository.findById(1)).thenReturn(Optional.of(order));
        when(orderItemRepository.save(any(OrderItem.class))).thenAnswer(invocation -> {
            OrderItem savedOrderItem = invocation.getArgument(0);
            savedOrderItem.setId(1);
            return savedOrderItem;
        });

        OrderItemDto createdOrderItemDto = orderItemService.createOrderItem(orderItemDto);
        assertNotNull(createdOrderItemDto);
        assertEquals(10, createdOrderItemDto.getProductId());
        assertEquals(100.0, createdOrderItemDto.getPrice());
        verify(productClient, times(1)).getProductById(10);
        verify(orderRepository, times(1)).findById(1);
        verify(orderItemRepository, times(1)).save(any(OrderItem.class));
    }

    @Test
    void testUpdateOrderItem() {
        OrderItemDto orderItemDto = new OrderItemDto();
        orderItemDto.setId(1);
        orderItemDto.setProductId(10);
        orderItemDto.setQuantity(5);
        orderItemDto.setOrderId(1);

        OrderItem existingOrderItem = new OrderItem();
        existingOrderItem.setId(1);
        existingOrderItem.setProductId(10);

        Order order = new Order();
        order.setId(1);

        Map<String, Object> product = Map.of("price", 100.0);

        when(orderItemRepository.findById(1)).thenReturn(Optional.of(existingOrderItem));
        when(orderRepository.findById(1)).thenReturn(Optional.of(order));
        when(productClient.getProductById(10)).thenReturn(product);
        when(orderItemRepository.save(any(OrderItem.class))).thenAnswer(invocation -> {
            OrderItem savedOrderItem = invocation.getArgument(0);
            savedOrderItem.setId(1);
            return savedOrderItem;
        });

        OrderItemDto updatedOrderItemDto = orderItemService.updateOrderItem(orderItemDto);
        assertNotNull(updatedOrderItemDto);
        assertEquals(10, updatedOrderItemDto.getProductId());
        assertEquals(100.0, updatedOrderItemDto.getPrice());
        verify(productClient, times(1)).getProductById(10);
        verify(orderRepository, times(1)).findById(1);
        verify(orderItemRepository, times(1)).save(any(OrderItem.class));
    }

    @Test
    void testDeleteOrderItem() {
        doNothing().when(orderItemRepository).deleteById(1);

        orderItemService.deleteOrderItem(1);
        verify(orderItemRepository, times(1)).deleteById(1);
    }
}

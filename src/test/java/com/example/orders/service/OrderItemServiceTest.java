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
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashMap;
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

    private OrderItemService orderItemService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        orderItemService = new OrderItemService(orderItemRepository, orderRepository, productClient);
    }

    @Test
    public void testGetOrderItemById() {
        OrderItem orderItem = new OrderItem();
        orderItem.setId(1);
        orderItem.setProductId(1);
        orderItem.setQuantity(2);
        orderItem.setPrice(100.0);

        when(orderItemRepository.findById(1)).thenReturn(Optional.of(orderItem));

        OrderItemDto orderItemDto = orderItemService.getOrderItemById(1);
        assertNotNull(orderItemDto);
        assertEquals(1, orderItemDto.getId());
    }

    @Test
    public void testCreateOrderItem() {
        OrderItemDto orderItemDto = new OrderItemDto();
        orderItemDto.setOrderId(1);
        orderItemDto.setProductId(1);
        orderItemDto.setQuantity(2);
        orderItemDto.setPrice(100.0);

        OrderItem orderItem = OrderItemMapper.INSTANCE.toEntity(orderItemDto);
        Order order = new Order();
        order.setId(1);

        Map<String, Object> product = new HashMap<>();
        product.put("price", 100.0);

        when(productClient.getProductById(1)).thenReturn(product);
        when(orderRepository.findById(1)).thenReturn(Optional.of(order));
        when(orderItemRepository.save(any(OrderItem.class))).thenReturn(orderItem);

        OrderItemDto createdOrderItem = orderItemService.createOrderItem(orderItemDto);
        assertNotNull(createdOrderItem);
        assertEquals(100.0, createdOrderItem.getPrice());
    }

    @Test
    public void testUpdateOrderItem() {
        OrderItemDto orderItemDto = new OrderItemDto();
        orderItemDto.setId(1);
        orderItemDto.setOrderId(1);
        orderItemDto.setProductId(1);
        orderItemDto.setQuantity(2);

        OrderItem orderItem = new OrderItem();
        orderItem.setId(1);
        Order order = new Order();
        order.setId(1);

        Map<String, Object> product = new HashMap<>();
        product.put("price", 100.0);

        when(orderItemRepository.findById(1)).thenReturn(Optional.of(orderItem));
        when(orderRepository.findById(1)).thenReturn(Optional.of(order));
        when(productClient.getProductById(1)).thenReturn(product);
        when(orderItemRepository.save(any(OrderItem.class))).thenReturn(orderItem);

        OrderItemDto updatedOrderItem = orderItemService.updateOrderItem(orderItemDto);
        assertNotNull(updatedOrderItem);
        assertEquals(100.0, updatedOrderItem.getPrice());
    }

    @Test
    public void testDeleteOrderItem() {
        doNothing().when(orderItemRepository).deleteById(1);
        orderItemService.deleteOrderItem(1);
        verify(orderItemRepository, times(1)).deleteById(1);
    }
}

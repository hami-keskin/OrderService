package com.example.orders.service;

import com.example.orders.client.ProductClient;
import com.example.orders.dto.OrderItemDto;
import com.example.orders.entity.Order;
import com.example.orders.entity.OrderItem;
import com.example.orders.repository.OrderItemRepository;
import com.example.orders.repository.OrderRepository;
import com.example.orders.util.TestDataOrderItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
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

    private Order order;
    private OrderItem orderItem;
    private OrderItemDto orderItemDto;

    @BeforeEach
    void setUp() {
        order = TestDataOrderItem.createOrder();
        orderItem = TestDataOrderItem.createOrderItem(order);
        orderItemDto = TestDataOrderItem.createOrderItemDto();
    }

    @Test
    public void testGetOrderItemById() {
        when(orderItemRepository.findById(1)).thenReturn(Optional.of(orderItem));
        OrderItemDto foundOrderItem = orderItemService.getOrderItemById(1);
        assertNotNull(foundOrderItem);
        assertEquals(1, foundOrderItem.getId());
    }

    @Test
    public void testCreateOrderItem() {
        when(orderRepository.findById(1)).thenReturn(Optional.of(order));
        when(productClient.getProductById(1)).thenReturn(Map.of("price", 100.0));
        when(orderItemRepository.save(any(OrderItem.class))).thenReturn(orderItem);

        OrderItemDto savedOrderItemDto = orderItemService.createOrderItem(orderItemDto);
        assertNotNull(savedOrderItemDto);
        assertEquals(orderItem.getId(), savedOrderItemDto.getId());
    }

    @Test
    public void testUpdateOrderItem() {
        when(orderItemRepository.findById(1)).thenReturn(Optional.of(orderItem));
        when(orderRepository.findById(1)).thenReturn(Optional.of(order));
        when(productClient.getProductById(1)).thenReturn(Map.of("price", 100.0));
        when(orderItemRepository.save(any(OrderItem.class))).thenReturn(orderItem);

        OrderItemDto updatedOrderItemDto = orderItemService.updateOrderItem(orderItemDto);
        assertNotNull(updatedOrderItemDto);
        assertEquals(orderItem.getId(), updatedOrderItemDto.getId());
    }

    @Test
    public void testDeleteOrderItem() {
        doNothing().when(orderItemRepository).deleteById(1);
        orderItemService.deleteOrderItem(1);
        verify(orderItemRepository, times(1)).deleteById(1);
    }

    @Test
    public void testGetOrderItemById_NotFound() {
        when(orderItemRepository.findById(1)).thenReturn(Optional.empty());
        OrderItemDto foundOrderItem = orderItemService.getOrderItemById(1);
        assertNull(foundOrderItem);
    }

    @Test
    public void testCreateOrderItem_InvalidProductId() {
        orderItemDto.setProductId(-1);
        assertThrows(IllegalArgumentException.class, () -> orderItemService.createOrderItem(orderItemDto));
    }

    @Test
    public void testUpdateOrderItem_NotFound() {
        when(orderItemRepository.findById(1)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> orderItemService.updateOrderItem(orderItemDto));
    }

    @Test
    public void testDeleteOrderItem_NotFound() {
        doThrow(new RuntimeException("OrderItem not found")).when(orderItemRepository).deleteById(1);
        assertThrows(RuntimeException.class, () -> orderItemService.deleteOrderItem(1));
    }
}

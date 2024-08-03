package com.example.orders.service;

import com.example.orders.dto.OrderItemDto;
import com.example.orders.entity.OrderItem;
import com.example.orders.mapper.OrderItemMapper;
import com.example.orders.repository.OrderItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class OrderItemServiceTest {

    @Mock
    private OrderItemRepository orderItemRepository;

    @Mock
    private OrderItemMapper orderItemMapper;

    private OrderItemService orderItemService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        orderItemService = new OrderItemService(orderItemRepository, orderItemMapper);
    }

    @Test
    void testGetOrderItemById() {
        OrderItem orderItem = new OrderItem();
        OrderItemDto orderItemDto = new OrderItemDto();
        when(orderItemRepository.findById(1)).thenReturn(Optional.of(orderItem));
        when(orderItemMapper.toDto(orderItem)).thenReturn(orderItemDto);

        Optional<OrderItemDto> result = orderItemService.getOrderItemById(1);
        assertTrue(result.isPresent());
        assertEquals(orderItemDto, result.get());
    }

    @Test
    void testCreateOrderItem() {
        OrderItem orderItem = new OrderItem();
        OrderItemDto orderItemDto = new OrderItemDto();
        orderItemDto.setOrderId(1);
        orderItemDto.setProductId(1);
        orderItemDto.setQuantity(10);
        orderItemDto.setPrice(100.0);

        when(orderItemMapper.toEntity(orderItemDto)).thenReturn(orderItem);
        when(orderItemRepository.save(orderItem)).thenReturn(orderItem);
        when(orderItemMapper.toDto(orderItem)).thenReturn(orderItemDto);

        OrderItemDto result = orderItemService.createOrderItem(orderItemDto);
        assertEquals(orderItemDto, result);
    }

    @Test
    void testUpdateOrderItem() {
        OrderItem orderItem = new OrderItem();
        OrderItemDto orderItemDto = new OrderItemDto();
        orderItemDto.setId(1);
        orderItemDto.setOrderId(1);
        orderItemDto.setProductId(1);
        orderItemDto.setQuantity(20);
        orderItemDto.setPrice(200.0);

        when(orderItemMapper.toEntity(orderItemDto)).thenReturn(orderItem);
        when(orderItemRepository.save(orderItem)).thenReturn(orderItem);
        when(orderItemMapper.toDto(orderItem)).thenReturn(orderItemDto);

        OrderItemDto result = orderItemService.updateOrderItem(orderItemDto);
        assertEquals(orderItemDto, result);
    }

    @Test
    void testDeleteOrderItem() {
        doNothing().when(orderItemRepository).deleteById(1);
        orderItemService.deleteOrderItem(1);
        verify(orderItemRepository, times(1)).deleteById(1);
    }
}

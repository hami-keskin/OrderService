package com.example.OrderService.service;

import com.example.OrderService.client.ProductDto;
import com.example.OrderService.client.ProductServiceClient;
import com.example.OrderService.dto.OrderDto;
import com.example.OrderService.dto.OrderItemDto;
import com.example.OrderService.entity.Order;
import com.example.OrderService.entity.OrderItem;
import com.example.OrderService.mapper.OrderItemMapper;
import com.example.OrderService.mapper.OrderMapper;
import com.example.OrderService.repository.OrderRepository;
import com.example.OrderService.repository.OrderItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class OrderServiceTest {

    private OrderService orderService;
    private OrderRepository orderRepository;
    private OrderItemRepository orderItemRepository;
    private OrderMapper orderMapper;
    private OrderItemMapper orderItemMapper;
    private ProductServiceClient productServiceClient;

    @BeforeEach
    public void setUp() {
        orderRepository = Mockito.mock(OrderRepository.class);
        orderItemRepository = Mockito.mock(OrderItemRepository.class);
        orderMapper = Mockito.mock(OrderMapper.class);
        orderItemMapper = Mockito.mock(OrderItemMapper.class);
        productServiceClient = Mockito.mock(ProductServiceClient.class);

        orderService = new OrderService(orderRepository, orderItemRepository, orderMapper, orderItemMapper, productServiceClient, self);  // self ekleyin
    }

    @Test
    public void testGetOrderById() {
        Order order = new Order();
        order.setId(1);
        when(orderRepository.findById(1)).thenReturn(Optional.of(order));
        when(orderMapper.toDto(any(Order.class))).thenReturn(new OrderDto());

        Optional<OrderDto> orderDto = orderService.getOrderById(1);

        assertThat(orderDto).isPresent();
        verify(orderRepository, times(1)).findById(1);
    }

    @Test
    public void testCreateOrder() {
        Order order = new Order();
        order.setOrderDate(LocalDateTime.now());
        order.setTotalAmount(0.0);
        order.setStatus(1);

        when(orderMapper.toEntity(any(OrderDto.class))).thenReturn(order);
        when(orderRepository.save(any(Order.class))).thenReturn(order);
        when(orderMapper.toDto(any(Order.class))).thenReturn(new OrderDto());

        OrderDto orderDto = new OrderDto();
        OrderDto createdOrder = orderService.createOrder(orderDto);

        assertThat(createdOrder).isNotNull();
        verify(orderRepository, times(1)).save(any(Order.class));
    }

    @Test
    public void testUpdateOrder() {
        Order order = new Order();
        order.setId(1);

        when(orderMapper.toEntity(any(OrderDto.class))).thenReturn(order);
        when(orderRepository.save(any(Order.class))).thenReturn(order);
        when(orderMapper.toDto(any(Order.class))).thenReturn(new OrderDto());

        OrderDto orderDto = new OrderDto();
        OrderDto updatedOrder = orderService.updateOrder(1, orderDto);

        assertThat(updatedOrder).isNotNull();
        verify(orderRepository, times(1)).save(any(Order.class));
    }

    @Test
    public void testDeleteOrder() {
        doNothing().when(orderRepository).deleteById(1);

        orderService.deleteOrder(1);

        verify(orderRepository, times(1)).deleteById(1);
    }

    @Test
    public void testAddOrderItem() {
        Order order = new Order();
        order.setId(1);
        order.setOrderItems(new ArrayList<>());

        OrderItem orderItem = new OrderItem();
        orderItem.setProductId(2);
        orderItem.setQuantity(3);
        orderItem.setOrder(order);

        when(orderRepository.findById(1)).thenReturn(Optional.of(order));
        when(orderItemMapper.toEntity(any(OrderItemDto.class))).thenReturn(orderItem);
        when(orderItemRepository.save(any(OrderItem.class))).thenReturn(orderItem);
        when(orderItemMapper.toDto(any(OrderItem.class))).thenReturn(new OrderItemDto());

        ProductDto productDto = new ProductDto();
        productDto.setPrice(100.0);
        when(productServiceClient.getProductById(2)).thenReturn(productDto);

        OrderItemDto orderItemDto = new OrderItemDto();
        orderItemDto.setProductId(2);
        orderItemDto.setQuantity(3);
        OrderItemDto createdOrderItem = orderService.addOrderItem(1, orderItemDto);

        assertThat(createdOrderItem).isNotNull();
        verify(orderRepository, times(1)).findById(1);
        verify(orderItemRepository, times(1)).save(any(OrderItem.class));
    }

    @Test
    public void testUpdateOrderItem() {
        Order order = new Order();
        order.setId(1);
        order.setOrderItems(new ArrayList<>());

        OrderItem orderItem = new OrderItem();
        orderItem.setId(1);
        orderItem.setProductId(2);
        orderItem.setQuantity(5);
        orderItem.setOrder(order);
        order.getOrderItems().add(orderItem);

        when(orderRepository.findById(1)).thenReturn(Optional.of(order));
        when(orderItemRepository.findById(1)).thenReturn(Optional.of(orderItem));
        when(orderItemRepository.save(any(OrderItem.class))).thenReturn(orderItem);
        when(orderItemMapper.toDto(any(OrderItem.class))).thenReturn(new OrderItemDto());

        ProductDto productDto = new ProductDto();
        productDto.setPrice(100.0);
        when(productServiceClient.getProductById(2)).thenReturn(productDto);

        OrderItemDto orderItemDto = new OrderItemDto();
        orderItemDto.setQuantity(5);
        OrderItemDto updatedOrderItem = orderService.updateOrderItem(1, 1, orderItemDto);

        assertThat(updatedOrderItem).isNotNull();
        verify(orderRepository, times(1)).findById(1);
        verify(orderItemRepository, times(1)).findById(1);
        verify(orderItemRepository, times(1)).save(any(OrderItem.class));
    }

    @Test
    public void testDeleteOrderItem() {
        Order order = new Order();
        order.setId(1);
        order.setOrderItems(new ArrayList<>());

        OrderItem orderItem = new OrderItem();
        orderItem.setId(1);
        orderItem.setOrder(order);
        order.getOrderItems().add(orderItem);

        when(orderRepository.findById(1)).thenReturn(Optional.of(order));
        when(orderItemRepository.findById(1)).thenReturn(Optional.of(orderItem));
        doNothing().when(orderItemRepository).delete(any(OrderItem.class));

        orderService.deleteOrderItem(1, 1);

        verify(orderRepository, times(1)).findById(1);
        verify(orderItemRepository, times(1)).findById(1);
        verify(orderItemRepository, times(1)).delete(any(OrderItem.class));
    }

    @Test
    public void testGetOrderItemsByOrderId() {
        Order order = new Order();
        order.setId(1);
        order.setOrderItems(Collections.singletonList(new OrderItem()));

        when(orderRepository.findById(1)).thenReturn(Optional.of(order));
        when(orderItemMapper.toDto(any(OrderItem.class))).thenReturn(new OrderItemDto());

        List<OrderItemDto> orderItems = orderService.getOrderItemsByOrderId(1);

        assertThat(orderItems).isNotEmpty();
        verify(orderRepository, times(1)).findById(1);
    }
}

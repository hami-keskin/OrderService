package com.example.OrderService.controller;

import com.example.OrderService.dto.OrderDto;
import com.example.OrderService.dto.OrderItemDto;
import com.example.OrderService.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @GetMapping("/{id}")
    public ResponseEntity<OrderDto> getOrderById(@PathVariable Integer id) {
        Optional<OrderDto> orderDto = orderService.getOrderById(id);
        return orderDto.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<OrderDto> createOrder(@RequestBody OrderDto orderDto) {
        OrderDto createdOrder = orderService.createOrder(orderDto);
        return ResponseEntity.ok(createdOrder);
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrderDto> updateOrder(@PathVariable Integer id, @RequestBody OrderDto orderDto) {
        OrderDto updatedOrder = orderService.updateOrder(id, orderDto);
        return ResponseEntity.ok(updatedOrder);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Integer id) {
        orderService.deleteOrder(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{orderId}/items")
    public ResponseEntity<OrderItemDto> addOrderItem(@PathVariable Integer orderId, @RequestBody OrderItemDto orderItemDto) {
        OrderItemDto createdOrderItem = orderService.addOrderItem(orderId, orderItemDto);
        return ResponseEntity.ok(createdOrderItem);
    }

    @PutMapping("/{orderId}/items/{orderItemId}")
    public ResponseEntity<OrderItemDto> updateOrderItem(@PathVariable Integer orderId, @PathVariable Integer orderItemId, @RequestBody OrderItemDto orderItemDto) {
        OrderItemDto updatedOrderItem = orderService.updateOrderItem(orderId, orderItemId, orderItemDto);
        return ResponseEntity.ok(updatedOrderItem);
    }

    @DeleteMapping("/{orderId}/items/{orderItemId}")
    public ResponseEntity<Void> deleteOrderItem(@PathVariable Integer orderId, @PathVariable Integer orderItemId) {
        orderService.deleteOrderItem(orderId, orderItemId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{orderId}/items")
    public ResponseEntity<List<OrderItemDto>> getOrderItemsByOrderId(@PathVariable Integer orderId) {
        List<OrderItemDto> orderItems = orderService.getOrderItemsByOrderId(orderId);
        return ResponseEntity.ok(orderItems);
    }
}

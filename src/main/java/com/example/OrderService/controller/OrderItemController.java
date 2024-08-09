package com.example.OrderService.controller;

import com.example.OrderService.dto.OrderDto;
import com.example.OrderService.dto.OrderItemDto;
import com.example.OrderService.service.OrderItemService;
import com.example.OrderService.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders/{orderId}/items")
@RequiredArgsConstructor
public class OrderItemController {
    private final OrderItemService orderItemService;
    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderItemDto> addOrderItem(@PathVariable Integer orderId, @RequestBody OrderItemDto orderItemDto) {
        OrderItemDto createdOrderItem = orderItemService.addOrderItem(orderService.getOrderById(orderId).orElseThrow(() -> new RuntimeException("Order not found")), orderItemDto);
        return ResponseEntity.ok(createdOrderItem);
    }

    @PutMapping("/{orderItemId}")
    public ResponseEntity<OrderItemDto> updateOrderItem(@PathVariable Integer orderId, @PathVariable Integer orderItemId, @RequestBody OrderItemDto orderItemDto) {
        OrderItemDto updatedOrderItem = orderItemService.updateOrderItem(orderService.getOrderById(orderId).orElseThrow(() -> new RuntimeException("Order not found")), orderItemId, orderItemDto);
        return ResponseEntity.ok(updatedOrderItem);
    }

    @DeleteMapping("/{orderItemId}")
    public ResponseEntity<Void> deleteOrderItem(@PathVariable Integer orderId, @PathVariable Integer orderItemId) {
        orderItemService.deleteOrderItem(orderService.getOrderById(orderId).orElseThrow(() -> new RuntimeException("Order not found")), orderItemId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<OrderItemDto>> getOrderItemsByOrderId(@PathVariable Integer orderId) {
        List<OrderItemDto> orderItems = orderItemService.getOrderItemsByOrderId(orderService.getOrderById(orderId).orElseThrow(() -> new RuntimeException("Order not found")));
        return ResponseEntity.ok(orderItems);
    }
}

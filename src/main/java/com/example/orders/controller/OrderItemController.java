package com.example.orders.controller;

import com.example.orders.dto.OrderItemDto;
import com.example.orders.service.OrderItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/orderItems")
@RequiredArgsConstructor
public class OrderItemController {
    private final OrderItemService orderItemService;

    @GetMapping("/{id}")
    public ResponseEntity<OrderItemDto> getOrderItemById(@PathVariable Integer id) {
        Optional<OrderItemDto> orderItemDto = orderItemService.getOrderItemById(id);
        return orderItemDto.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<OrderItemDto> createOrderItem(@RequestBody OrderItemDto orderItemDto) {
        OrderItemDto createdOrderItem = orderItemService.createOrderItem(orderItemDto);
        return ResponseEntity.ok(createdOrderItem);
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrderItemDto> updateOrderItem(@PathVariable Integer id, @RequestBody OrderItemDto orderItemDto) {
        orderItemDto.setId(id);
        OrderItemDto updatedOrderItem = orderItemService.updateOrderItem(orderItemDto);
        return ResponseEntity.ok(updatedOrderItem);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrderItem(@PathVariable Integer id) {
        orderItemService.deleteOrderItem(id);
        return ResponseEntity.noContent().build();
    }
}

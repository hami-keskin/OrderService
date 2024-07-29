package com.example.orders.controller;

import com.example.orders.dto.OrderItemDto;
import com.example.orders.service.OrderItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/order-items")
public class OrderItemController {

    @Autowired
    private OrderItemService orderItemService;

    @GetMapping
    public List<OrderItemDto> getAllOrderItems() {
        return orderItemService.getAllOrderItems();
    }

    @GetMapping("/{id}")
    public OrderItemDto getOrderItemById(@PathVariable Integer id) {
        return orderItemService.getOrderItemById(id);
    }

    @PostMapping
    public OrderItemDto createOrderItem(@RequestBody OrderItemDto orderItemDto) {
        return orderItemService.createOrderItem(orderItemDto);
    }

    @PutMapping("/{id}")
    public OrderItemDto updateOrderItem(@PathVariable Integer id, @RequestBody OrderItemDto orderItemDto) {
        return orderItemService.updateOrderItem(id, orderItemDto);
    }

    @DeleteMapping("/{id}")
    public void deleteOrderItem(@PathVariable Integer id) {
        orderItemService.deleteOrderItem(id);
    }
}

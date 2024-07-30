package com.example.orders.controller;

import com.example.orders.dto.OrderDto;
import com.example.orders.service.OrderService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/{id}")
    public OrderDto getOrderById(@PathVariable Integer id) {
        return orderService.getOrderById(id);
    }

    @PostMapping
    public OrderDto createOrder(@RequestBody OrderDto orderDto) {
        return orderService.createOrder(orderDto);
    }

    @PutMapping("/{id}")
    public OrderDto updateOrder(@PathVariable Integer id, @RequestBody OrderDto orderDto) {
        orderDto.setId(id);
        return orderService.updateOrder(orderDto);
    }

    @DeleteMapping("/{id}")
    public void deleteOrder(@PathVariable Integer id) {
        orderService.deleteOrder(id);
    }
}

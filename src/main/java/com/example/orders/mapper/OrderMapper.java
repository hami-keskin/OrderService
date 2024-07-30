package com.example.orders.mapper;

import com.example.orders.dto.OrderDto;
import com.example.orders.entity.Order;
import org.mapstruct.Mapper;
import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    OrderDto toOrderDto(Order order);
    Order toOrder(OrderDto orderDto);
    List<OrderDto> toOrderDtos(List<Order> orders);
}

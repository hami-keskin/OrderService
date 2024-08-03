package com.example.orders.mapper;

import com.example.orders.dto.OrderDto;
import com.example.orders.entity.Order;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    OrderDto toDto(Order order);
    Order toEntity(OrderDto orderDto);
}

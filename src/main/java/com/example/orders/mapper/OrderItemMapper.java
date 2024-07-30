package com.example.orders.mapper;

import com.example.orders.dto.OrderItemDto;
import com.example.orders.entity.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface OrderItemMapper {
    OrderItemMapper INSTANCE = Mappers.getMapper(OrderItemMapper.class);

    @Mapping(source = "order.id", target = "orderId")
    OrderItemDto toDto(OrderItem orderItem);

    @Mapping(target = "order.id", source = "orderId")
    OrderItem toEntity(OrderItemDto orderItemDto);
}
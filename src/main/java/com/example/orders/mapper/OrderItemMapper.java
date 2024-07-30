package com.example.orders.mapper;

import com.example.orders.dto.OrderItemDto;
import com.example.orders.entity.OrderItem;
import org.mapstruct.Mapper;
import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderItemMapper {
    OrderItemDto toOrderItemDto(OrderItem orderItem);
    OrderItem toOrderItem(OrderItemDto orderItemDto);
    List<OrderItemDto> toOrderItemDtos(List<OrderItem> orderItems);
}

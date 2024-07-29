package com.example.order_service.mapper;

import com.example.order_service.dto.ShippingAddressDto;
import com.example.order_service.entity.ShippingAddress;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ShippingAddressMapper {
    ShippingAddressMapper INSTANCE = Mappers.getMapper(ShippingAddressMapper.class);

    ShippingAddressDto toDto(ShippingAddress shippingAddress);
    ShippingAddress toEntity(ShippingAddressDto shippingAddressDto);
}

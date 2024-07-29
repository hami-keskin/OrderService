package com.example.orders.mapper;

import com.example.orders.dto.ShippingAddressDto;
import com.example.orders.entity.ShippingAddress;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ShippingAddressMapper {
    ShippingAddressMapper INSTANCE = Mappers.getMapper(ShippingAddressMapper.class);

    ShippingAddressDto toDto(ShippingAddress shippingAddress);
    ShippingAddress toEntity(ShippingAddressDto shippingAddressDto);
}

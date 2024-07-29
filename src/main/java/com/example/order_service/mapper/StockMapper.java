package com.example.order_service.mapper;

import com.example.order_service.dto.StockDto;
import com.example.order_service.entity.Stock;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface StockMapper {
    StockMapper INSTANCE = Mappers.getMapper(StockMapper.class);

    StockDto toDto(Stock stock);
    Stock toEntity(StockDto stockDto);
}

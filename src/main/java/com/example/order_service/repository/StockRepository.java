package com.example.order_service.repository;

import com.example.order_service.entity.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StockRepository extends JpaRepository<Stock, Integer> {
    Stock findByProductId(Integer productId);
}

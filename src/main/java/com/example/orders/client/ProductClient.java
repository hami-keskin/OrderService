package com.example.orders.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Map;

@FeignClient(name = "product-service", url = "${feign.client.config.product-service.url}")
public interface ProductClient {

    @GetMapping("/products/{id}")
    Map<String, Object> getProductById(@PathVariable("id") Integer id);
}

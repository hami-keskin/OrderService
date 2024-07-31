package com.example.orders.exception;

public class OrderNotFoundException extends RuntimeException {
    public OrderNotFoundException(String message) {
        super(message);
    }
}

public class OrderItemNotFoundException extends RuntimeException {
    public OrderItemNotFoundException(String message) {
        super(message);
    }
}

public class ProductNotFoundException extends RuntimeException {
    public ProductNotFoundException(String message) {
        super(message);
    }
}

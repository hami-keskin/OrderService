package com.example.OrderService.controller;

import com.example.OrderService.client.GlobalAdviceClient;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final GlobalAdviceClient globalAdviceClient;

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleAllExceptions(Exception ex) {
        String globalErrorMessage = globalAdviceClient.getGlobalError(ex.getMessage());
        return ResponseEntity.status(500).body(globalErrorMessage);
    }

    @ExceptionHandler(FeignException.class)
    public ResponseEntity<String> handleFeignStatusException(FeignException e) {
        String globalErrorMessage = globalAdviceClient.getGlobalError(e.getMessage());
        return ResponseEntity.status(e.status()).body(globalErrorMessage);
    }
}

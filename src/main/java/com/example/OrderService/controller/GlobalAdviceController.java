package com.example.OrderService.controller;

import com.example.OrderService.exception.RecordNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
@Slf4j
public class GlobalAdviceController {

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseEntity<String> exceptionHandler(Throwable exception) {
        log.error("Exception: {}", exception.getMessage());
        if (exception instanceof RecordNotFoundException) {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
        } else if (exception instanceof IllegalArgumentException) {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
        } else {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

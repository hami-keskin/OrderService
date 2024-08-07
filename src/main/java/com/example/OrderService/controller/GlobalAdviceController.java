package com.example.OrderService.controller;

import com.example.OrderService.exception.RecordNotFoundException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDateTime;

@ControllerAdvice
@Slf4j
public class GlobalAdviceController {

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseEntity<ErrorResponse> exceptionHandler(Throwable exception) {
        log.error("Exception: {}", exception.getMessage(), exception);
        HttpStatus status;
        if (exception instanceof RecordNotFoundException) {
            status = HttpStatus.NOT_FOUND;
        } else if (exception instanceof IllegalArgumentException) {
            status = HttpStatus.BAD_REQUEST;
        } else {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        ErrorResponse errorResponse = new ErrorResponse(LocalDateTime.now(), status.value(), exception.getMessage());
        return new ResponseEntity<>(errorResponse, status);
    }
}

@Getter
@RequiredArgsConstructor
class ErrorResponse {
    private final LocalDateTime timestamp;
    private final int status;
    private final String error;
}

package com.example.OrderService.aspect;

import com.example.OrderService.annotation.RequestLogger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggerAspect {
    private static final Logger logger = LoggerFactory.getLogger(LoggerAspect.class);

    // Method level @RequestLogger
    @Pointcut("@annotation(com.example.OrderService.annotation.RequestLogger)")
    private void methodRequestLogger() {
    }

    // Class level @RequestLogger
    @Pointcut("@within(com.example.OrderService.annotation.RequestLogger)")
    private void classRequestLogger() {
    }

    // Combined pointcut for both method and class level
    @Pointcut("methodRequestLogger() || classRequestLogger()")
    private void requestLoggerMethods() {
    }

    // Around advice: Hedef metodun öncesinde ve sonrasında çalışır
    @Around("requestLoggerMethods()")
    public Object logRequestResponse(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();

        // Yöntem çağrısı öncesi loglama
        logger.info("Executing method: {} with arguments: {}", joinPoint.getSignature(), joinPoint.getArgs());

        Object result;
        try {
            result = joinPoint.proceed();
        } catch (Throwable throwable) {
            // Hata durumunda loglama
            logger.error("Exception in method: {} with cause: {}", joinPoint.getSignature(), throwable.getCause() != null ? throwable.getCause() : "NULL");
            throw throwable;
        }

        long timeTaken = System.currentTimeMillis() - startTime;
        // Yöntem çağrısı sonrası loglama
        logger.info("Method executed: {} returned: {} in: {}ms", joinPoint.getSignature(), result, timeTaken);

        return result;
    }
}

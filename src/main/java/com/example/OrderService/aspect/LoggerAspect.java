package com.example.OrderService.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggerAspect {
    private static final Logger logger = LoggerFactory.getLogger(LoggerAspect.class);

    @Pointcut("execution(public * com.example..*(..))")
    private void publicMethods() {
        // Pointcut for all public methods in the application
    }

    @Around("publicMethods()")
    public Object logRequestResponse(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();

        // Log the request details
        logger.info("Executing method: {} with arguments: {}", joinPoint.getSignature(), joinPoint.getArgs());

        Object result;
        try {
            result = joinPoint.proceed();
        } catch (Throwable throwable) {
            // Log the exception
            logger.error("Exception in method: {} with cause: {}", joinPoint.getSignature(), throwable.getCause() != null ? throwable.getCause() : "NULL");
            throw throwable;
        }

        long timeTaken = System.currentTimeMillis() - startTime;
        // Log the response details
        logger.info("Method executed: {} returned: {} in: {}ms", joinPoint.getSignature(), result, timeTaken);

        return result;
    }
}

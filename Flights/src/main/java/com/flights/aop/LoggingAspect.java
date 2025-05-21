package com.flights.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class LoggingAspect {

    @Pointcut("execution(public * com.flights.services..*(..))")
    public void serviceMethods() {}

    @Before("serviceMethods()")
    public void logBeforeMethods(JoinPoint joinPoint) {
        log.info("Entering Method: {} with args: {}", joinPoint.getSignature(), joinPoint.getArgs());
    }

    @AfterReturning(pointcut = "serviceMethods()", returning = "result")
    public void logAfterReturning(JoinPoint joinPoint, Object result) {
        log.info("Method {} returned: {}", joinPoint.getSignature(), result);
    }

    @AfterThrowing(pointcut = "serviceMethods()", throwing = "ex")
    public void logAfterException(JoinPoint joinPoint, Exception ex) {
        log.error("Method {} threw exception: {}", joinPoint.getSignature(), ex.getMessage());
    }

    @After("serviceMethods()")
    public void logAfterMethods(JoinPoint joinPoint) {
        log.info("Exiting Method: {}", joinPoint.getSignature());
    }

}

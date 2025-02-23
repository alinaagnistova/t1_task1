package ru.alina.t1_task1.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Aspect
@Component
public class LoggingAspect {
    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

    @Before("execution(* ru.alina.t1_task1.service.TaskService.*(..))")
    public void logMethodExecution(JoinPoint joinPoint) {
        logger.info("Method {} has started execution", joinPoint.getSignature());
    }
    @AfterThrowing(pointcut= "@annotation(ru.alina.t1_task1.aspect.annotation.CustomExceptionHandling)", throwing = "ex")
    public void logMethodThrowing(JoinPoint joinPoint, Exception ex) {
        logger.warn("Method {} threw an exception", joinPoint.getSignature().toShortString());
        logger.warn("Exception {} with message {}", ex.getClass().getName(), ex.getMessage());
    }

    @Around("execution(* ru.alina.t1_task1.service.TaskService.addTask(..))")
    public Object logMethodElapsedTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        long endTime = System.currentTimeMillis();
        long elapsedTime = endTime - startTime;
        logger.info("Method {} completed in {} ms", joinPoint.getSignature().toShortString(), elapsedTime);
        return result;
    }
    @AfterReturning(pointcut = "@annotation(ru.alina.t1_task1.aspect.annotation.CustomLogging)", returning = "result")
    public void logMethodReturning(JoinPoint joinPoint, Object result) {
        logger.info("Method {} returned {}", joinPoint.getSignature().toShortString(), result.toString());
    }


}

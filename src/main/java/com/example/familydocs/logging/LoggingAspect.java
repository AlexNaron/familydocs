package com.example.familydocs.logging;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.CodeSignature;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.reflect.Parameter;


@Aspect
@Component
public class LoggingAspect {

    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

    @SuppressWarnings("EmptyMethod")
    @Pointcut("execution(* com.example.familydocs.controller..*.*(..))")
    public void controllerLayer() {}

    @SuppressWarnings("EmptyMethod")
    @Pointcut("execution(* com.example.familydocs.service..*.*(..))")
    public void serviceLayer() {}

    @SuppressWarnings("EmptyMethod")
    @Pointcut("controllerLayer() || serviceLayer()")
    public void applicationFlow() {}

    @Before("applicationFlow()")
    public void logBefore(JoinPoint joinPoint) {

        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        String className = joinPoint.getTarget().getClass().getSimpleName();
        String methodName = methodSignature.getName();
        Object[] args = joinPoint.getArgs();
        CodeSignature codeSignature = (CodeSignature) joinPoint.getSignature();
        String[] parameterNames = codeSignature.getParameterNames();
        Parameter[] parameters = methodSignature.getMethod().getParameters();

        StringBuilder logMessage = new StringBuilder("Entering method: " + methodName + " with selective parameters: ");

        for (int i = 0; i < parameters.length; i++) {
            if (parameters[i].isAnnotationPresent(LoggableParameter.class)) {
                logMessage.append(parameterNames[i]).append("=").append(args[i]).append("; ");
            }
        }

        if (className.endsWith("Controller")) {
            logger.info("Controller call: {}.{} | {}", className, methodName, logMessage);
        } else if (className.endsWith("Service")) {
            logger.debug("Service call: {}.{} | {}", className, methodName, logMessage);
        }
    }

    @AfterReturning(pointcut = "applicationFlow()", returning = "result")
    public void logAfterReturning(JoinPoint joinPoint, Object result) {

        String className = joinPoint.getTarget().getClass().getSimpleName();
        if (className.endsWith("Controller")) {
            logger.info("Controller exit: {}.{} | Method returned: with result = {}",
                    joinPoint.getTarget().getClass().getSimpleName(),
                    joinPoint.getSignature().toShortString(),
                    result);
        } else if (className.endsWith("Service")) {
            logger.debug("Service exit: {}.{} | Method returned: with result = {}",
                    joinPoint.getTarget().getClass().getSimpleName(),
                    joinPoint.getSignature().toShortString(),
                    result);
        }
    }

    @AfterThrowing(pointcut = "applicationFlow()", throwing = "error")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable error) {

        logger.error("An exception has been thrown in {}.{} with message = {}",
                joinPoint.getTarget().getClass().getSimpleName(),
                joinPoint.getSignature().toShortString(),
                error.getMessage());
    }
}
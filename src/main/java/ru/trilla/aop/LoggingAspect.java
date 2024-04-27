package ru.trilla.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;

@Component
@Slf4j
@Aspect
public class LoggingAspect {

    @Around("(within(@org.springframework.stereotype.Service *) || within(@org.springframework.stereotype.Component *)) && execution(public * *(..))")
    public Object logMethod(ProceedingJoinPoint joinPoint) throws Throwable {
        final var startTime = Instant.now();
        log.info(
                "Calling: {}.{} with arguments: {}",
                joinPoint.getTarget().getClass(),
                joinPoint.getSignature().getName(),
                joinPoint.getArgs()
        );
        final var result = joinPoint.proceed();
        log.trace("Method called for {} ms", Duration.between(startTime, Instant.now()).toMillisPart());
        return result;
    }
}

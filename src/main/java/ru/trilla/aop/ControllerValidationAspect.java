package ru.trilla.aop;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import ru.trilla.exception.ValidationException;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
public class ControllerValidationAspect {
    private static final int DEFAULT_STRING_BUILDER_CAPACITY = 150;
    private final Validator validator;

    @Before("within(@org.springframework.web.bind.annotation.RestController *) && execution(public * *(..))")
    void validate(JoinPoint joinPoint) {
        final var violations = Arrays.stream(joinPoint.getArgs())
                .flatMap(arg -> validator.validate(arg).stream())
                .collect(Collectors.toSet());
        if (!violations.isEmpty()) {
            log.error(
                    "Validation violations! Method: {}, violations: {}",
                    joinPoint.getSignature(),
                    violations
            );
            throw new ValidationException(String.format(
                    "Ошибки валидации запроса: [%s]",
                    buildViolationsDescription(violations)
            ));
        }
    }

    private String buildViolationsDescription(Set<ConstraintViolation<Object>> violations) {
        final var sb = new StringBuilder(DEFAULT_STRING_BUILDER_CAPACITY);
        violations.forEach(violation -> sb.append(
                String.format("(сообщение: %s, путь к атрибуту: %s),", violation.getMessage(), violation.getPropertyPath())
        ));
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }
}

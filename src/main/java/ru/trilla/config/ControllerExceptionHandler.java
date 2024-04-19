package ru.trilla.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.trilla.dto.ErrorResponseDto;
import ru.trilla.exception.RestApiException;

import java.time.LocalDateTime;
import java.util.Objects;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class ControllerExceptionHandler {

    @ExceptionHandler(RestApiException.class)
    public ResponseEntity<ErrorResponseDto> handleRestApiException(RestApiException exception, HttpServletRequest request) {
        return handleException(exception.getStatus(), request, exception, true);
    }

    @ExceptionHandler(Throwable.class)
    public ResponseEntity<ErrorResponseDto> handleCommonError(Throwable throwable, HttpServletRequest request) {
        return handleException(HttpStatus.INTERNAL_SERVER_ERROR, request, throwable, false);
    }

    @ExceptionHandler({
            MethodArgumentNotValidException.class,
            MissingServletRequestParameterException.class,
            MissingPathVariableException.class,
            ConstraintViolationException.class,
            ValidationException.class
    })
    public ResponseEntity<ErrorResponseDto> handleBadRequest(Throwable throwable, HttpServletRequest request) {
        return handleException(HttpStatus.BAD_REQUEST, request, throwable, false);
    }

    public ResponseEntity<ErrorResponseDto> handleException(
            HttpStatus status,
            HttpServletRequest request,
            Throwable throwable,
            boolean isBusiness
    ) {
        log.info("Request exception: {}, message: {}", throwable.getClass(), throwable.getMessage());
        if (isBusiness) {
            return buildResponse(
                    status,
                    request,
                    Objects.nonNull(throwable.getMessage()) ? throwable.getMessage() : "Неопределённая ошибка"
            );
        }
        return buildResponse(
                status,
                request,
                "Внутренняя ошибка системы"
        );
    }

    private ResponseEntity<ErrorResponseDto> buildResponse(
            HttpStatus status,
            HttpServletRequest request,
            String message
    ) {
        return ResponseEntity
                .status(status)
                .body(
                        new ErrorResponseDto(
                                status,
                                message,
                                LocalDateTime.now(),
                                resolveRequestPath(request)
                        )
                );
    }

    private String resolveRequestPath(HttpServletRequest request) {
        if (Objects.nonNull(request.getQueryString())) {
            return String.format("%s?%s", request.getRequestURI(), request.getQueryString());
        }
        return request.getRequestURI();
    }
}

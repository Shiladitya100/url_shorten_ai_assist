package com.schwab.urlshortener.exception;

import com.schwab.urlshortener.dto.ApiErrorResponse;
import com.schwab.urlshortener.dto.FieldErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import java.time.Clock;
import java.time.OffsetDateTime;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    private final Clock clock = Clock.systemUTC();

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleValidationException(
            MethodArgumentNotValidException exception,
            HttpServletRequest request
    ) {
        List<FieldErrorResponse> fieldErrors = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(fieldError -> new FieldErrorResponse(fieldError.getField(), fieldError.getDefaultMessage()))
                .toList();

        log.debug("Validation failure path={} fieldErrorCount={}", request.getRequestURI(), fieldErrors.size());
        return buildResponse(
                HttpStatus.BAD_REQUEST,
                "Request validation failed",
                request.getRequestURI(),
                fieldErrors
        );
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiErrorResponse> handleConstraintViolationException(
            ConstraintViolationException exception,
            HttpServletRequest request
    ) {
        List<FieldErrorResponse> fieldErrors = exception.getConstraintViolations()
                .stream()
                .map(violation -> new FieldErrorResponse(
                        violation.getPropertyPath().toString(),
                        violation.getMessage()
                ))
                .toList();

        log.debug("Constraint violation path={} fieldErrorCount={}", request.getRequestURI(), fieldErrors.size());
        return buildResponse(
                HttpStatus.BAD_REQUEST,
                "Request validation failed",
                request.getRequestURI(),
                fieldErrors
        );
    }

    @ExceptionHandler({UrlMappingNotFoundException.class, UrlMappingNotRedirectableException.class})
    public ResponseEntity<ApiErrorResponse> handleNotFound(RuntimeException exception, HttpServletRequest request) {
        log.info("Handled not found path={} reason={}", request.getRequestURI(), exception.getMessage());
        return buildResponse(HttpStatus.NOT_FOUND, exception.getMessage(), request.getRequestURI(), List.of());
    }

    @ExceptionHandler(UrlMappingExpiredException.class)
    public ResponseEntity<ApiErrorResponse> handleExpired(
            UrlMappingExpiredException exception,
            HttpServletRequest request
    ) {
        log.info("Handled expired mapping path={} reason={}", request.getRequestURI(), exception.getMessage());
        return buildResponse(HttpStatus.GONE, exception.getMessage(), request.getRequestURI(), List.of());
    }

    @ExceptionHandler(ShortCodeGenerationException.class)
    public ResponseEntity<ApiErrorResponse> handleShortCodeGenerationFailure(
            ShortCodeGenerationException exception,
            HttpServletRequest request
    ) {
        log.error("Handled short code generation failure path={} reason={}", request.getRequestURI(), exception.getMessage());
        return buildResponse(HttpStatus.SERVICE_UNAVAILABLE, exception.getMessage(), request.getRequestURI(), List.of());
    }

    private ResponseEntity<ApiErrorResponse> buildResponse(
            HttpStatus status,
            String message,
            String path,
            List<FieldErrorResponse> fieldErrors
    ) {
        ApiErrorResponse response = new ApiErrorResponse(
                OffsetDateTime.now(clock),
                status.value(),
                status.getReasonPhrase(),
                message,
                path,
                fieldErrors
        );
        return ResponseEntity.status(status).body(response);
    }
}

package com.puthi.commentapi.exception;

import com.puthi.commentapi.error.ProblemResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestControllerAdvice(basePackages = "com.puthi.commentapi")
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    private String correlationId(HttpServletRequest req) {
        String id = req.getHeader("X-Request-ID");
        return (id == null || id.isBlank()) ? UUID.randomUUID().toString() : id;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ProblemResponse> handleValidation(MethodArgumentNotValidException ex, HttpServletRequest req) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        List<ProblemResponse.FieldErrorItem> items = ex.getBindingResult().getFieldErrors().stream()
                .map(fe -> new ProblemResponse.FieldErrorItem(
                        fe.getField(),
                        fe.getDefaultMessage(),
                        fe.getRejectedValue() == null ? null : String.valueOf(fe.getRejectedValue())
                ))
                .collect(Collectors.toList());

        String detail = items.stream()
                .map(it -> it.field() + ": " + it.message())
                .collect(Collectors.joining(", "));

        ProblemResponse body = new ProblemResponse(
                "https://errors.puthi.com/validation-error",
                "Validation Failed",
                status.value(),
                detail.isBlank() ? "Request validation failed" : detail,
                req.getRequestURI(),
                "VALIDATION_FAILED",
                correlationId(req),
                Instant.now(),
                items
        );
        return ResponseEntity.status(status).body(body);
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ProblemResponse> handleNotFound(NoHandlerFoundException ex, HttpServletRequest req) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        ProblemResponse body = new ProblemResponse(
                "https://errors.puthi.com/not-found",
                "Not Found",
                status.value(),
                "No handler for " + ex.getHttpMethod() + " " + ex.getRequestURL(),
                req.getRequestURI(),
                "NOT_FOUND",
                correlationId(req),
                Instant.now(),
                null
        );
        return ResponseEntity.status(status).body(body);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ProblemResponse> handleUncaught(Exception ex, HttpServletRequest req) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        log.error("Unhandled exception at {}: {}", req.getRequestURI(), ex.getMessage(), ex);
        ProblemResponse body = new ProblemResponse(
                "https://errors.puthi.com/internal-error",
                "Internal Server Error",
                status.value(),
                ex.getMessage(),
                req.getRequestURI(),
                "INTERNAL_ERROR",
                correlationId(req),
                Instant.now(),
                null
        );
        return ResponseEntity.status(status).body(body);
    }
}

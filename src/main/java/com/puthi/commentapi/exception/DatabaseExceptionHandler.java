package com.puthi.commentapi.exception;

import com.puthi.commentapi.error.ProblemResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.hibernate.exception.JDBCConnectionException;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLTransientException;
import java.time.Instant;
import java.util.UUID;

@RestControllerAdvice(basePackages = "com.puthi.commentapi")
public class DatabaseExceptionHandler {

    private String correlationId(HttpServletRequest req) {
        String id = req.getHeader("X-Request-ID");
        return (id == null || id.isBlank()) ? UUID.randomUUID().toString() : id;
    }

    @ExceptionHandler({
            CannotGetJdbcConnectionException.class,
            JDBCConnectionException.class,
            SQLTransientException.class,
            DataAccessException.class
    })
    public ResponseEntity<ProblemResponse> handleDbErrors(Exception ex, HttpServletRequest req) {
        HttpStatus status = HttpStatus.SERVICE_UNAVAILABLE;
        ProblemResponse body = new ProblemResponse(
                "https://errors.puthi.com/db-unavailable",
                "Database Unavailable",
                status.value(),
                ex.getMessage(),
                req.getRequestURI(),
                "DB_UNAVAILABLE",
                correlationId(req),
                Instant.now(),
                null
        );
        return ResponseEntity.status(status).body(body);
    }
}

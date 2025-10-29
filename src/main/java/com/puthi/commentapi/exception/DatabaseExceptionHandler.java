package com.puthi.commentapi.exception;

import org.hibernate.exception.JDBCConnectionException;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.servlet.http.HttpServletRequest;
import java.time.Instant;
import java.sql.SQLTransientException;

@RestControllerAdvice
public class DatabaseExceptionHandler {

    @ExceptionHandler({
            CannotGetJdbcConnectionException.class,
            JDBCConnectionException.class,
            SQLTransientException.class,
            DataAccessException.class
    })
    public ResponseEntity<ErrorResponse> handleDbErrors(Exception ex, HttpServletRequest req) {
        HttpStatus status = HttpStatus.SERVICE_UNAVAILABLE;
        ErrorResponse body = new ErrorResponse(
                Instant.now().toString(),
                req.getRequestURI(),
                status.value(),
                status.getReasonPhrase(),
                ex.getClass().getSimpleName() + ": " + ex.getMessage()
        );
        return ResponseEntity.status(status).body(body);
    }
}


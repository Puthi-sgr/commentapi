package com.puthi.commentapi.error;

import java.time.Instant;
import java.util.List;

/** Top-level error body (Problem Details + practical fields). */
public record ProblemResponse(
        String type,
        String title,
        int status,
        String detail,
        String instance,
        String code,
        String correlationId,
        Instant timestamp,
        List<FieldErrorItem> errors
) {
    /** Field-level validation error item. */
    public record FieldErrorItem(
            String field,
            String message,
            String rejected
    ) {}
}


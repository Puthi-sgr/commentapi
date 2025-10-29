package com.puthi.commentapi.dto.health;

public record DbHealthResponse(
        String component,
        String status,
        long latencyMs,
        String databaseProduct,
        String databaseVersion,
        String error
) {}


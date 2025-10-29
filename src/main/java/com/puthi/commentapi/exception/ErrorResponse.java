package com.puthi.commentapi.exception;

public record ErrorResponse(
        String timestamp,
        String path,
        int status,
        String error,
        String message
) {}


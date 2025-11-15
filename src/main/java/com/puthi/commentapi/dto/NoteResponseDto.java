package com.puthi.commentapi.dto;

import java.time.Instant;

/**
 * Output DTO representing a Note resource.
 */
public record NoteResponseDto(
        Long id,
        String title,
        String content,
        Long ownerId,
        Instant createdAt,
        Instant updatedAt
) {}

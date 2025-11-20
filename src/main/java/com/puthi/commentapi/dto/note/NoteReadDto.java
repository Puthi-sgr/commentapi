package com.puthi.commentapi.dto.note;

import java.time.Instant;

/**
 * Output DTO representing a Note resource.
 */
public record NoteReadDto(
        Long id,
        String title,
        String content,
        Long ownerId,
        Instant createdAt,
        Instant updatedAt
) {}

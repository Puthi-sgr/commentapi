package com.puthi.commentapi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Input DTO for creating/updating a Note.
 */
public record NoteCreateDto(
        @NotBlank @Size(max = 500) String title,
        @Size(max = 500) String content
) {}

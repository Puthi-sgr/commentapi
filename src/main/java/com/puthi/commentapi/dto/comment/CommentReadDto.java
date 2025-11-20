package com.puthi.commentapi.dto.comment;

import java.time.Instant;

public record CommentReadDto(
        Long id,
        Long noteId,
        Long parentId,

        String content,
        Long authorId,
        String authorName,

        String path,
        int depth,

        Instant createdAt,
        Instant updatedAt,

        Long replyCount
) {}


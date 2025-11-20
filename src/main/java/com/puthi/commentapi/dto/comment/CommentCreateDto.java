package com.puthi.commentapi.dto.comment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CommentCreateDto (
        @NotBlank @Size(max = 500) String content //Checks for blank space and ""
){ }

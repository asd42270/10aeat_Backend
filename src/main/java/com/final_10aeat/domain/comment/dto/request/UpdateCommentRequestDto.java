package com.final_10aeat.domain.comment.dto.request;

import jakarta.validation.constraints.NotBlank;

public record UpdateCommentRequestDto(
    @NotBlank
    String content
) {

}

package com.final_10aeat.domain.comment.dto.request;

import jakarta.validation.constraints.NotBlank;

public record CreateCommentRequestDto(
    Long parentCommentId,
    @NotBlank
    String content
) {

}

package com.final_10aeat.domain.comment.dto.response;

import java.time.LocalDateTime;

public record CommentResponseDto(
    Long id,
    String content,
    LocalDateTime createdAt,
    Long parentCommentId,
    boolean isManager,
    String writer
) {

}

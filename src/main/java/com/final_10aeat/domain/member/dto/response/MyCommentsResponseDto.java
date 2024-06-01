package com.final_10aeat.domain.member.dto.response;

import java.time.LocalDateTime;

public record MyCommentsResponseDto(
    Long articleId,
    String content,
    LocalDateTime createdAt,
    String writer
) {

}

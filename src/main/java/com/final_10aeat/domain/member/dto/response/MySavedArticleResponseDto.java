package com.final_10aeat.domain.member.dto.response;

import java.time.LocalDateTime;

public record MySavedArticleResponseDto(
    Long ArticleId,
    String title,
    LocalDateTime createdAt,
    String Writer
){

}

package com.final_10aeat.domain.repairArticle.dto.response;

import java.time.LocalDateTime;

public record RepairArticleResponseDto(
    Long id,
    String category,
    String managerName,
    String progress,
    String title,
    LocalDateTime startConstruction,
    LocalDateTime endConstruction,
    int commentCount,
    int viewCount,
    boolean isSave,
    boolean issueCheck,
    boolean isNewArticle,
    String imageUrl
) {

}

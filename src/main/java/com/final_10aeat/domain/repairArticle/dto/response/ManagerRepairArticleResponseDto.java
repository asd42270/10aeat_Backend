package com.final_10aeat.domain.repairArticle.dto.response;

import java.time.LocalDateTime;

public record ManagerRepairArticleResponseDto(
    Long id,
    String category,
    String managerName,
    String progress,
    String title,
    LocalDateTime startConstruction,
    LocalDateTime endConstruction,
    LocalDateTime createdAt,
    LocalDateTime updatedAt,
    int commentCount,
    int viewCount,
    String imageUrl
) {

}

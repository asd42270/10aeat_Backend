package com.final_10aeat.domain.repairArticle.dto.response;

import com.final_10aeat.common.enumclass.ArticleCategory;
import com.final_10aeat.common.enumclass.Progress;
import java.time.LocalDateTime;

public record SearchManagerRepairArticleResponseDto(
    Long id,
    ArticleCategory category,
    String managerName,
    Progress progress,
    String title,
    LocalDateTime startConstruction,
    LocalDateTime endConstruction,
    LocalDateTime createdAt,
    LocalDateTime updatedAt,
    Integer commentCount,
    Integer viewCount
) {

}

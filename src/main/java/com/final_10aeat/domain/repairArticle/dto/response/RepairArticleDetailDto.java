package com.final_10aeat.domain.repairArticle.dto.response;

import java.time.LocalDateTime;
import java.util.List;

public record RepairArticleDetailDto(
    String category,
    String progress,
    boolean isSave,
    Long managerId,
    String managerName,
    LocalDateTime createdAt,
    LocalDateTime updatedAt,
    String title,
    String content,
    List<String> imageUrls,
    LocalDateTime startConstruction,
    LocalDateTime endConstruction,
    String company,
    String companyWebsite
) {

}

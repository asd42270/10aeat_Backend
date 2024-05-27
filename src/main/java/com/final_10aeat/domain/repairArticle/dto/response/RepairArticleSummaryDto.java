package com.final_10aeat.domain.repairArticle.dto.response;

public record RepairArticleSummaryDto(
    long total,
    long inProgressAndPending,
    long complete
) {

}

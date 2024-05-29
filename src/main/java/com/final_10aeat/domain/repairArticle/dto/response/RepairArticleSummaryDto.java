package com.final_10aeat.domain.repairArticle.dto.response;

public record RepairArticleSummaryDto(
    long total,
    long inProgressAndPending,
    boolean inProgressAndPendingRedDot,
    long complete,
    boolean completeRedDot
) {

}

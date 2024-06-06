package com.final_10aeat.domain.articleIssue.dto.response;

import java.time.LocalDateTime;

public record IssueHistoryResponseDto(
    Long id,
    String title,
    boolean isActive,
    LocalDateTime createdAt
) {

}

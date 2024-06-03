package com.final_10aeat.domain.articleIssue.dto.request;

import lombok.Builder;

@Builder
public record IssueUpdateRequestDto(
        String title,
        String content
) {
}

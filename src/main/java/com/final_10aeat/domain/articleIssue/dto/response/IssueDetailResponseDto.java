package com.final_10aeat.domain.articleIssue.dto.response;

import lombok.Builder;

@Builder
public record IssueDetailResponseDto(
    String title,
    String content
) {

}

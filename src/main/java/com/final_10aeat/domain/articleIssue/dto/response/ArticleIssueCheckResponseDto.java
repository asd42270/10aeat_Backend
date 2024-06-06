package com.final_10aeat.domain.articleIssue.dto.response;

import lombok.Builder;

@Builder
public record ArticleIssueCheckResponseDto(
    String title,
    String content
) {

}

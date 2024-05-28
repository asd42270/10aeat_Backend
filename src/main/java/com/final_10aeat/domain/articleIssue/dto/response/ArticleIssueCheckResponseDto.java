package com.final_10aeat.domain.articleIssue.dto.response;

import jakarta.validation.constraints.AssertTrue;
import lombok.Builder;

@Builder
public record ArticleIssueCheckResponseDto(
        Long id,
        String title,
        String content,
        @AssertTrue
        Boolean check
) {
}

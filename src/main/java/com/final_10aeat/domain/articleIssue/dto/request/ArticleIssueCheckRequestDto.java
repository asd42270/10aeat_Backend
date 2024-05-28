package com.final_10aeat.domain.articleIssue.dto.request;

import jakarta.validation.constraints.AssertTrue;

public record ArticleIssueCheckRequestDto(
        @AssertTrue
        Boolean check
) {
}

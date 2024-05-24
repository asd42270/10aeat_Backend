package com.final_10aeat.domain.articleIssue.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ArticleIssuePublishRequestDto(
        @NotBlank
        String title,
        @NotBlank
        String content
) {
}

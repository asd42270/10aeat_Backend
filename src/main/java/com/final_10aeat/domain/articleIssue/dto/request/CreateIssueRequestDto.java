package com.final_10aeat.domain.articleIssue.dto.request;

import jakarta.validation.constraints.NotBlank;

public record CreateIssueRequestDto(
        @NotBlank
        String title,
        @NotBlank
        String content
) {
}

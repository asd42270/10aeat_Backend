package com.final_10aeat.domain.articleIssue.dto.request;

import lombok.Builder;

@Builder
public record UpdateIssueRequestDto(
    String title,
    String content
) {

}

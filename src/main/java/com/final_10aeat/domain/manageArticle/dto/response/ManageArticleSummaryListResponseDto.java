package com.final_10aeat.domain.manageArticle.dto.response;

import java.util.List;

public record ManageArticleSummaryListResponseDto(
    Integer complete,
    Integer inprogress,
    Integer pending,
    List<Long> hasIssue
) {

}

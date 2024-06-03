package com.final_10aeat.domain.manageArticle.dto.response;

import java.util.List;

public record SummaryManageArticleResponse(
    Integer complete,
    Integer inprogress,
    Integer pending,
    List<Long> hasIssue
) {

}

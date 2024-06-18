package com.final_10aeat.domain.manageArticle.dto.response;

import com.final_10aeat.common.enumclass.ManagePeriod;
import lombok.Builder;

@Builder
public record ManageArticleListResponseDto(
    Long id,
    ManagePeriod period,
    Integer periodCount,
    String title,
    Integer allSchedule,
    Integer completedSchedule,
    Long issueId
) {

}

package com.final_10aeat.domain.manageArticle.dto.response;

import com.final_10aeat.common.enumclass.ManagePeriod;
import com.final_10aeat.common.enumclass.Progress;
import java.util.List;
import lombok.Builder;

@Builder
public record DetailManageArticleResponse(
    ManagePeriod period,
    Integer periodCount,
    String title,
    Long issueId,
    Progress progress,
    String legalBasis,
    String target,
    String responsibility,
    String note,
    List<ManageScheduleResponse> manageSchedule
) {

}

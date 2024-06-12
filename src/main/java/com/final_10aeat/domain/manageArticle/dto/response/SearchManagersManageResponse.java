package com.final_10aeat.domain.manageArticle.dto.response;

import com.final_10aeat.common.enumclass.ManagePeriod;
import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public record SearchManagersManageResponse(
    Long id,
    ManagePeriod period,
    Integer periodCount,
    String title,
    Integer allSchedule,
    Integer completedSchedule,
    LocalDateTime currentSchedules
) {

}
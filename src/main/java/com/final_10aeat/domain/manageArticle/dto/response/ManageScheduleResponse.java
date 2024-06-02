package com.final_10aeat.domain.manageArticle.dto.response;

import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public record ManageScheduleResponse(
    Long manageScheduleId,
    Boolean isComplete,
    LocalDateTime scheduleStart,
    LocalDateTime scheduleEnd
) {

}

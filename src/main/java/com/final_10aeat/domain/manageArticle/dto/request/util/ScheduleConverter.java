package com.final_10aeat.domain.manageArticle.dto.request.util;

import com.final_10aeat.domain.manageArticle.dto.request.ScheduleRequestDto;
import com.final_10aeat.domain.manageArticle.entity.ManageSchedule;

public class ScheduleConverter {
    public static ManageSchedule toSchedule(ScheduleRequestDto request) {
        return ManageSchedule.builder()
            .complete(false)
            .scheduleStart(request.scheduleStart())
            .scheduleEnd(request.scheduleEnd())
            .build();
    }
}

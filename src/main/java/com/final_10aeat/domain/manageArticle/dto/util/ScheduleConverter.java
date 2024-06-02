package com.final_10aeat.domain.manageArticle.dto.util;

import com.final_10aeat.domain.manageArticle.dto.request.ScheduleRequestDto;
import com.final_10aeat.domain.manageArticle.dto.response.ManageScheduleResponse;
import com.final_10aeat.domain.manageArticle.entity.ManageArticle;
import com.final_10aeat.domain.manageArticle.entity.ManageSchedule;

public class ScheduleConverter {

    public static ManageSchedule toSchedule(ScheduleRequestDto request, ManageArticle article) {
        return ManageSchedule.builder()
            .complete(false)
            .scheduleStart(request.scheduleStart())
            .scheduleEnd(request.scheduleEnd())
            .manageArticle(article)
            .build();
    }

    public static ManageScheduleResponse toScheduleResponse(ManageSchedule schedule){
        return ManageScheduleResponse.builder()
            .manageScheduleId(schedule.getId())
            .isComplete(schedule.isComplete())
            .scheduleStart(schedule.getScheduleStart())
            .scheduleEnd(schedule.getScheduleEnd())
            .build();
    }
}

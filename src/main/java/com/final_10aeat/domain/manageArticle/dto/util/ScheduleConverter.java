package com.final_10aeat.domain.manageArticle.dto.util;

import com.final_10aeat.domain.manageArticle.dto.request.CreateScheduleRequestDto;
import com.final_10aeat.domain.manageArticle.dto.response.ManageScheduleResponseDto;
import com.final_10aeat.domain.manageArticle.entity.ManageArticle;
import com.final_10aeat.domain.manageArticle.entity.ManageSchedule;

public class ScheduleConverter {

    public static ManageSchedule toSchedule(CreateScheduleRequestDto request, ManageArticle article) {
        return ManageSchedule.builder()
            .complete(false)
            .year(request.scheduleStart().getYear())
            .month(request.scheduleStart().getMonthValue())
            .scheduleStart(request.scheduleStart())
            .scheduleEnd(request.scheduleEnd())
            .manageArticle(article)
            .build();
    }

    public static ManageScheduleResponseDto toScheduleResponse(ManageSchedule schedule){
        return ManageScheduleResponseDto.builder()
            .manageScheduleId(schedule.getId())
            .isComplete(schedule.isComplete())
            .scheduleStart(schedule.getScheduleStart())
            .scheduleEnd(schedule.getScheduleEnd())
            .build();
    }
}

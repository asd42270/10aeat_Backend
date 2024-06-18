package com.final_10aeat.domain.manageArticle.dto.request;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public record CreateScheduleRequestDto(
    @NotNull(message = "시작 날짜는 반드시 존재해야 합니다.")
    LocalDateTime scheduleStart,
    LocalDateTime scheduleEnd
) {

}

package com.final_10aeat.domain.alarm.dto.response;

import com.final_10aeat.domain.alarm.entity.enumtype.AlarmType;
import lombok.Builder;

@Builder
public record AlarmResponseDto(
        Long alarmId,
        AlarmType alarmType,
        Long targetId,
        Boolean checked
) {
}

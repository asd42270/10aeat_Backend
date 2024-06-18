package com.final_10aeat.domain.manager.dto.response;

import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public record GetManagerInfoResponseDto(
    String email,
    String name,
    String phoneNumber,
    LocalDateTime lunchBreakStart,
    LocalDateTime lunchBreakEnd,
    String managerOffice,
    String affiliation
) {

}

package com.final_10aeat.domain.member.dto.response;

import lombok.Builder;

@Builder
public record MyBuildingInfoResponseDto(
        String officeName,
        Long buildingInfoId,
        String dong,
        String ho
) {
}

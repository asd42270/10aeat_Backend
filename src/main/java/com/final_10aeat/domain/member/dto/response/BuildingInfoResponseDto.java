package com.final_10aeat.domain.member.dto.response;

import lombok.Builder;

import java.util.List;

@Builder
public record BuildingInfoResponseDto(
        String officeName,
        Long buildingInfoId,
        String dong,
        String ho
) {
}

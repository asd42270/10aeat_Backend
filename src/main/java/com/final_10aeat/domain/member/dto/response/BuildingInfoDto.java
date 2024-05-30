package com.final_10aeat.domain.member.dto.response;

import lombok.Builder;

@Builder
public record BuildingInfoDto(
        Long buildingInfoId,
        String dong,
        String ho
) {
}

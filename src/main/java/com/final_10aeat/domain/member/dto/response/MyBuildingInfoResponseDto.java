package com.final_10aeat.domain.member.dto.response;

import lombok.Builder;

@Builder
public record MyBuildingInfoResponseDto(
    Long buildingInfoId,
    String officeName,
    String dong,
    String ho
) {

}

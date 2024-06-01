package com.final_10aeat.domain.member.dto.request;

import jakarta.validation.constraints.NotBlank;

public record BuildingInfoRequestDto(
    @NotBlank
    String dong,
    @NotBlank
    String ho
) {

}

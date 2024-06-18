package com.final_10aeat.domain.office.dto.request;

import jakarta.validation.constraints.NotBlank;

public record CreateOfficeRequestDto(
        String officeName,
        @NotBlank
        String address,
        Double mapX,
        Double mapY
) {
}

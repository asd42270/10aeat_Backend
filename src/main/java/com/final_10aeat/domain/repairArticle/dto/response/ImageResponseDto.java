package com.final_10aeat.domain.repairArticle.dto.response;

import lombok.Builder;

@Builder
public record ImageResponseDto(
        Long imageId,
        String imageUrl
) {
}

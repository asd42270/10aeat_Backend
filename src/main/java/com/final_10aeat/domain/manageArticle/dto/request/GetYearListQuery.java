package com.final_10aeat.domain.manageArticle.dto.request;

import org.springframework.data.domain.Pageable;

public record GetYearListQuery(
    Integer year,
    Long officeId,
    Pageable pageRequest
) {
    public static GetYearListQuery toQueryDto(
        Integer year, Long officeId, Pageable pageRequest
    ) {
        return new GetYearListQuery(
            year,
            officeId,
            pageRequest
        );
    }
}

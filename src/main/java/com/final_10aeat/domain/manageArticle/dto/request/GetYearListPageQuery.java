package com.final_10aeat.domain.manageArticle.dto.request;

import org.springframework.data.domain.Pageable;

public record GetYearListPageQuery(
    Integer year,
    Long officeId,
    Pageable pageRequest
) {
    public static GetYearListPageQuery toQueryDto(
        Integer year, Long officeId, Pageable pageRequest
    ) {
        return new GetYearListPageQuery(
            year,
            officeId,
            pageRequest
        );
    }
}

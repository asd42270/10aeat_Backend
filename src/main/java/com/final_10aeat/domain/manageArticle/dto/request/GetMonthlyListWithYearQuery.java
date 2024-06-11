package com.final_10aeat.domain.manageArticle.dto.request;

import org.springframework.data.domain.Pageable;

public record GetMonthlyListWithYearQuery(
    Integer year,
    Integer month,
    Long officeId,
    Pageable pageRequest
) {

    public static GetMonthlyListWithYearQuery toQueryDto(
        Integer year, Integer month, Long officeId, Pageable pageRequest
    ) {
        return new GetMonthlyListWithYearQuery(
            year,
            month,
            officeId,
            pageRequest
        );
    }
}

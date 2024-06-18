package com.final_10aeat.domain.manageArticle.dto.request;

import org.springframework.data.domain.Pageable;

public record GetMonthlyListByYearQuery(
    Integer year,
    Integer month,
    Long officeId,
    Pageable pageRequest
) {

    public static GetMonthlyListByYearQuery toQueryDto(
        Integer year, Integer month, Long officeId, Pageable pageRequest
    ) {
        return new GetMonthlyListByYearQuery(
            year,
            month,
            officeId,
            pageRequest
        );
    }
}

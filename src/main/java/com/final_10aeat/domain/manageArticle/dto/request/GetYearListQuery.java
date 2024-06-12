package com.final_10aeat.domain.manageArticle.dto.request;

public record GetYearListQuery(
    Integer year,
    Long officeId
) {

    public static GetYearListQuery toQueryDto(
        Integer year, Long officeId
    ) {
        return new GetYearListQuery(
            year,
            officeId
        );
    }
}

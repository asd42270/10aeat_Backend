package com.final_10aeat.domain.manageArticle.dto.request;

import com.final_10aeat.common.enumclass.ManagePeriod;

public record UpdateManageArticleRequestDto(
    ManagePeriod period,
    Integer periodCount,
    String title,
    String legalBasis,
    String target,
    String responsibility,
    String note
) {

}

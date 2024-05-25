package com.final_10aeat.domain.manageArticle.dto.request;

import com.final_10aeat.common.enumclass.ManagePeriod;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateManageArticleRequestDto(
    @NotNull
    ManagePeriod period,
    @NotNull
    Integer periodCount,
    @NotBlank
    String title,
    String legalBasis,
    String target,
    String responsibility,
    String note
) {

}

package com.final_10aeat.domain.repairArticle.dto.request;

import com.final_10aeat.common.enumclass.Progress;
import org.springframework.data.domain.Pageable;

public record SearchRepairArticleQueryDto(
    Long officeId,
    String keyword,
    Progress progress,
    Pageable pageRequest
) {

    public static SearchRepairArticleQueryDto toQueryDto(
        Long userOfficeId, String keyword, Progress progress, Pageable pageRequest
    ) {
        return new SearchRepairArticleQueryDto(userOfficeId, keyword, progress, pageRequest);
    }
}

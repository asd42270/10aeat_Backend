package com.final_10aeat.domain.repairArticle.dto.request;

import com.final_10aeat.common.enumclass.Progress;
import org.springframework.data.domain.Pageable;

public record SearchRepairArticleQuery(
    Long officeId,
    String keyword,
    Progress progress,
    Pageable pageRequest
) {

    public static SearchRepairArticleQuery toQueryDto(
        Long userOfficeId, String keyword, Progress progress, Pageable pageRequest
    ) {
        return new SearchRepairArticleQuery(userOfficeId, keyword, progress, pageRequest);
    }
}

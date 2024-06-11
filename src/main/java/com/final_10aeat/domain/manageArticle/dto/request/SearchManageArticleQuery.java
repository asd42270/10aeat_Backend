package com.final_10aeat.domain.manageArticle.dto.request;

import java.time.LocalDateTime;
import org.springframework.data.domain.Pageable;

public record SearchManageArticleQuery(
    LocalDateTime now,
    String keyword,
    Integer year,
    Integer month,
    Long officeId,
    Pageable pageRequest
) {

    public static SearchManageArticleQuery toSearchQuery(
        LocalDateTime now, String keyword, Integer year,
        Integer month, Long officeId, Pageable pageRequest
    ) {
        return new SearchManageArticleQuery(
            now,
            keyword,
            year,
            month,
            officeId,
            pageRequest
        );
    }
}

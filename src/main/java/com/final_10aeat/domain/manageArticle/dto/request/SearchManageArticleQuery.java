package com.final_10aeat.domain.manageArticle.dto.request;

import java.time.LocalDateTime;
import lombok.Builder;
import org.springframework.data.domain.Pageable;

@Builder
public record SearchManageArticleQuery(
    LocalDateTime now,
    String keyword,
    Integer year,
    Integer month,
    Long officeId,
    Pageable pageRequest
) {

}

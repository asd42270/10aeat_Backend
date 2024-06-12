package com.final_10aeat.domain.repairArticle.dto.request;

import com.final_10aeat.common.enumclass.ArticleCategory;
import com.final_10aeat.common.enumclass.Progress;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

public record CreateRepairArticleRequestDto(
    @NotNull(message = "카테고리는 필수 값 입니다.")
    ArticleCategory category,
    @NotNull(message = "진행상황은 필수 값 입니다.")
    Progress progress,
    @NotBlank(message = "제목을 입력해주세요.")
    String title,
    @NotBlank(message = "내용을 입력해주세요.")
    String content,
    LocalDateTime constructionStart,
    LocalDateTime constructionEnd,
    String repairCompany,
    String repairCompanyWebsite,
    List<Long> imageIds
) {

}

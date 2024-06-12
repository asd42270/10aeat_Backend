package com.final_10aeat.domain.repairArticle.repository;

import com.final_10aeat.common.enumclass.ArticleCategory;
import com.final_10aeat.common.enumclass.Progress;
import com.final_10aeat.domain.repairArticle.dto.request.SearchRepairArticleQueryDto;
import com.final_10aeat.domain.repairArticle.entity.RepairArticle;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RepairArticleQueryDslRepository {

    Page<RepairArticle> searchByTextAnsOfficeId(
        Long userOfficeId, String search, Pageable pageRequest
    );

    List<RepairArticle> findSoftDeletedBefore(LocalDateTime cutoffDate);

    Page<RepairArticle> findByOfficeIdAndProgressInAndCategoryOrderByIdDesc(
        Long officeId, List<Progress> progresses, ArticleCategory category, Pageable pageable,List<Long> checkedIssueIds);

    List<RepairArticle> findByOfficeIdAndProgressIn(Long officeId, List<Progress> progresses);

    Page<RepairArticle> findAll(SearchRepairArticleQueryDto queryDto);
}

package com.final_10aeat.domain.manageArticle.repository;

import com.final_10aeat.common.enumclass.Progress;
import com.final_10aeat.domain.manageArticle.dto.request.GetMonthlyListWithYearQuery;
import com.final_10aeat.domain.manageArticle.dto.request.GetYearListQuery;
import com.final_10aeat.domain.manageArticle.entity.ManageArticle;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ManageArticleQueryDslRepository {

    Page<ManageArticle> searchByOfficeIdAndText(
        Long userOfficeId, String search, Pageable pageRequest
    );

    Page<ManageArticle> findAllByUnDeletedOfficeIdAndScheduleYearAndProgress(
        Long userOfficeId, Integer year, Pageable pageRequest, List<Progress> progresses
    );

    Page<ManageArticle> findAllByYear(GetYearListQuery queryDto);

    Page<ManageArticle> findAllByYearAndMonthly(GetMonthlyListWithYearQuery queryDto);
}

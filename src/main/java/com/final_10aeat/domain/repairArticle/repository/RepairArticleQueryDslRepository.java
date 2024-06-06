package com.final_10aeat.domain.repairArticle.repository;

import com.final_10aeat.domain.repairArticle.entity.RepairArticle;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RepairArticleQueryDslRepository {

    Page<RepairArticle> searchByTextAnsOfficeId(
        Long userOfficeId, String search, Pageable pageRequest
    );
}

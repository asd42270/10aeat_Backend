package com.final_10aeat.domain.manageArticle.repository;

import com.final_10aeat.domain.manageArticle.entity.ManageArticle;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ManageArticleQueryDslRepository {

    Page<ManageArticle> searchByOfficeIdAndText(
        Long userOfficeId, String search, Pageable pageRequest
    );
}

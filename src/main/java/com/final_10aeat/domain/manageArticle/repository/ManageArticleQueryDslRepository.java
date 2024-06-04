package com.final_10aeat.domain.manageArticle.repository;

import com.final_10aeat.domain.manageArticle.entity.ManageArticle;
import java.util.List;

public interface ManageArticleQueryDslRepository {

    List<ManageArticle> searchByOfficeIdAndText(Long userOfficeId,String search);
}

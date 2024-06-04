package com.final_10aeat.domain.repairArticle.repository;

import com.final_10aeat.domain.repairArticle.entity.RepairArticle;
import java.util.List;

public interface RepairArticleQueryDslRepository {

    List<RepairArticle> searchByText(Long userOfficeId, String search);
}

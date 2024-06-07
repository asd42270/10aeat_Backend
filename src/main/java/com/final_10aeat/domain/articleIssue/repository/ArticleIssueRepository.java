package com.final_10aeat.domain.articleIssue.repository;

import com.final_10aeat.domain.articleIssue.entity.ArticleIssue;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleIssueRepository extends JpaRepository<ArticleIssue, Long> {

    List<ArticleIssue> findByManageArticleIdAndDeletedAtIsNull(Long manageArticleId);

    List<ArticleIssue> findByRepairArticleIdAndDeletedAtIsNull(Long repairArticleId);
}

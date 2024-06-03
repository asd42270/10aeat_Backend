package com.final_10aeat.domain.articleIssue.repository;

import com.final_10aeat.domain.articleIssue.entity.ArticleIssue;
import com.final_10aeat.domain.manageArticle.entity.ManageArticle;
import com.final_10aeat.domain.repairArticle.entity.RepairArticle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ArticleIssueRepository extends JpaRepository<ArticleIssue, Long> {

    Optional<ArticleIssue> findFirstByManageArticleAndDeletedAtIsNullOrderByCreatedAtDesc(ManageArticle manageArticle);

    Optional<ArticleIssue> findFirstByRepairArticleAndDeletedAtIsNullOrderByCreatedAtDesc(RepairArticle repairArticle);

    Optional<ArticleIssue> findByIdAndDeletedAtIsNull(Long id);
}

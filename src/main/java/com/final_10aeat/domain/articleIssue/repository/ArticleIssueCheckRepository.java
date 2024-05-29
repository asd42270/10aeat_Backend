package com.final_10aeat.domain.articleIssue.repository;

import com.final_10aeat.domain.articleIssue.entity.ArticleIssueCheck;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ArticleIssueCheckRepository extends JpaRepository<ArticleIssueCheck, Long> {

    @Query("SELECT aic.articleIssue.id FROM ArticleIssueCheck aic WHERE aic.member.id = :memberId AND aic.checked = true")
    Set<Long> findCheckedIssueIdsByMember(@Param("memberId") Long memberId);
}

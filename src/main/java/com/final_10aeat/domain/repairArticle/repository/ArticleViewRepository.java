package com.final_10aeat.domain.repairArticle.repository;

import com.final_10aeat.domain.repairArticle.entity.ArticleView;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleViewRepository extends JpaRepository<ArticleView, Long> {

    boolean existsByArticleIdAndUserId(Long articleId, Long userId);
}

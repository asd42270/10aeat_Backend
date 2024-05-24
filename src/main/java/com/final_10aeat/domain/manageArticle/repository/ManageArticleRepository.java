package com.final_10aeat.domain.manageArticle.repository;

import com.final_10aeat.domain.manageArticle.entity.ManageArticle;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ManageArticleRepository extends JpaRepository<ManageArticle, Long> {
}

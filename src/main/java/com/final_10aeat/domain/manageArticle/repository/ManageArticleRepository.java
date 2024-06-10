package com.final_10aeat.domain.manageArticle.repository;

import com.final_10aeat.common.enumclass.Progress;
import com.final_10aeat.domain.manageArticle.entity.ManageArticle;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ManageArticleRepository extends JpaRepository<ManageArticle, Long>,
    ManageArticleQueryDslRepository {

    List<ManageArticle> findAllByDeletedAtBeforeAndDeletedAtNotNull(LocalDateTime createdAt);

    List<ManageArticle> findAllByOfficeIdAndDeletedAtNull(Long id);

    Optional<ManageArticle> findByIdAndDeletedAtNull(Long id);
}

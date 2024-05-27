package com.final_10aeat.domain.repairArticle.repository;

import com.final_10aeat.common.enumclass.ArticleCategory;
import com.final_10aeat.common.enumclass.Progress;
import com.final_10aeat.domain.repairArticle.entity.RepairArticle;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface RepairArticleRepository extends JpaRepository<RepairArticle, Long> {

    @Query("SELECT r FROM RepairArticle r WHERE r.deletedAt IS NOT NULL AND r.deletedAt < :cutoffDate")
    List<RepairArticle> findSoftDeletedBefore(LocalDateTime cutoffDate);

    long countByManagerId(Long managerId);

    long countByManagerIdAndProgressIn(Long managerId, List<Progress> progressList);

    long countByManagerIdAndProgress(Long managerId, Progress progress);

    @Query("SELECT ra FROM RepairArticle ra WHERE ra.office.id = :officeId " +
        "AND (:progresses IS NULL OR ra.progress IN :progresses) " +
        "AND (:category IS NULL OR ra.category = :category)")
    List<RepairArticle> findByOfficeIdAndProgressInAndCategory(
        @Param("officeId") Long officeId,
        @Param("progresses") List<Progress> progresses,
        @Param("category") ArticleCategory category);
}

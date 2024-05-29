package com.final_10aeat.domain.repairArticle.repository;

import com.final_10aeat.common.enumclass.ArticleCategory;
import com.final_10aeat.common.enumclass.Progress;
import com.final_10aeat.domain.repairArticle.entity.RepairArticle;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RepairArticleRepository extends JpaRepository<RepairArticle, Long> {

    @Query("SELECT r FROM RepairArticle r WHERE r.deletedAt IS NOT NULL AND r.deletedAt < :cutoffDate")
    List<RepairArticle> findSoftDeletedBefore(LocalDateTime cutoffDate);

    long countByOfficeIdAndProgressIn(@Param("officeId") Long officeId,
        @Param("progresses") List<Progress> progresses);

    long countByOfficeIdAndProgress(@Param("officeId") Long officeId,
        @Param("progress") Progress progress);

    long countByOfficeId(@Param("officeId") Long officeId); // 전체 게시글 수를 사무실 ID로 카운트

    @Query("SELECT ra FROM RepairArticle ra WHERE ra.office.id = :officeId " +
        "AND (:progresses IS NULL OR ra.progress IN :progresses) " +
        "AND (:category IS NULL OR ra.category = :category)")
    List<RepairArticle> findByOfficeIdAndProgressInAndCategory(
        @Param("officeId") Long officeId,
        @Param("progresses") List<Progress> progresses,
        @Param("category") ArticleCategory category);

    @Query("SELECT ra FROM RepairArticle ra WHERE ra.office.id = :officeId " +
        "AND ra.progress IN :progresses")
    List<RepairArticle> findByOfficeIdAndProgressIn(
        @Param("officeId") Long officeId,
        @Param("progresses") List<Progress> progresses);
}

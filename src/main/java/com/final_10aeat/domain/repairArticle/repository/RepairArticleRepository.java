package com.final_10aeat.domain.repairArticle.repository;

import com.final_10aeat.domain.repairArticle.entity.RepairArticle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface RepairArticleRepository extends JpaRepository<RepairArticle, Long> {

    @Query("SELECT r FROM RepairArticle r WHERE r.deletedAt IS NOT NULL AND r.deletedAt < :cutoffDate")
    List<RepairArticle> findSoftDeletedBefore(LocalDateTime cutoffDate);
}

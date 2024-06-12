package com.final_10aeat.domain.repairArticle.repository;

import com.final_10aeat.domain.repairArticle.entity.RepairArticleImage;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface RepairArticleImageRepository extends JpaRepository<RepairArticleImage, Long> {

    @Query("SELECT r.id FROM RepairArticleImage r WHERE r.repairArticle IS NULL OR r.repairArticle.id NOT IN (SELECT a.id FROM RepairArticle a)")
    List<Long> findIdsOfOrphanAndNonExistentRepairArticleImages();

    @Transactional
    @Modifying
    void deleteAllByIdIn(List<Long> ids);
}

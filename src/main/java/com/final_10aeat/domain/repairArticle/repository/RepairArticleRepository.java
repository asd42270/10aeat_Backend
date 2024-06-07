package com.final_10aeat.domain.repairArticle.repository;

import com.final_10aeat.common.enumclass.Progress;
import com.final_10aeat.domain.repairArticle.entity.RepairArticle;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

public interface RepairArticleRepository extends JpaRepository<RepairArticle, Long>,
    RepairArticleQueryDslRepository {

    long countByOfficeIdAndProgressIn(@Param("officeId") Long officeId,
        @Param("progresses") List<Progress> progresses);

    long countByOfficeIdAndProgress(@Param("officeId") Long officeId,
        @Param("progress") Progress progress);

    long countByOfficeId(@Param("officeId") Long officeId);
}

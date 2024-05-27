package com.final_10aeat.domain.save.repository;

import com.final_10aeat.domain.member.entity.Member;
import com.final_10aeat.domain.repairArticle.entity.RepairArticle;
import com.final_10aeat.domain.save.entity.ArticleSave;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleSaveRepository extends JpaRepository<ArticleSave, Long> {

    boolean existsByRepairArticleAndMember(RepairArticle repairArticle, Member member);

    Optional<ArticleSave> findByRepairArticleAndMember(RepairArticle repairArticle, Member member);
}

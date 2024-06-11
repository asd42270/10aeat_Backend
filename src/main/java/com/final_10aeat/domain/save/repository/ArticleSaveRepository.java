package com.final_10aeat.domain.save.repository;

import com.final_10aeat.domain.member.entity.Member;
import com.final_10aeat.domain.repairArticle.entity.RepairArticle;
import com.final_10aeat.domain.save.entity.ArticleSave;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ArticleSaveRepository extends JpaRepository<ArticleSave, Long> {

    boolean existsByRepairArticleAndMember(RepairArticle repairArticle, Member member);

    boolean existsByRepairArticleIdAndMemberId(Long repairArticleId, Long memberId);

    Optional<ArticleSave> findByRepairArticleAndMember(RepairArticle repairArticle, Member member);

    @Query("SELECT a.repairArticle.id FROM ArticleSave a WHERE a.member.id = :memberId AND a.repairArticle.id IN :articleIds")
    Set<Long> findSavedArticleIdsByMember(@Param("memberId") Long memberId,
        @Param("articleIds") List<Long> articleIds);

    @Query("SELECT a FROM ArticleSave a WHERE a.member = :member AND a.repairArticle.office.id = :officeId")
    List<ArticleSave> findByMemberAndOffice(@Param("member") Member member,
        @Param("officeId") Long officeId);

    List<ArticleSave> findAllByRepairArticleId(Long repairArticleId);
}

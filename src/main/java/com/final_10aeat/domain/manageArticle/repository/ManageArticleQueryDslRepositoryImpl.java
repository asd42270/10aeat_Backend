package com.final_10aeat.domain.manageArticle.repository;

import com.final_10aeat.domain.manageArticle.entity.ManageArticle;
import com.final_10aeat.domain.manageArticle.entity.QManageArticle;
import com.final_10aeat.domain.repairArticle.entity.QRepairArticle;
import com.final_10aeat.domain.repairArticle.entity.RepairArticle;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ManageArticleQueryDslRepositoryImpl implements ManageArticleQueryDslRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<ManageArticle> searchByOfficeIdAndText(Long userOfficeId, String search) {
        QManageArticle manageArticle = QManageArticle.manageArticle;

        return queryFactory.selectFrom(manageArticle)
            .where(manageArticle.title.contains(search)
                .or(manageArticle.legalBasis.contains(search))
                .or(manageArticle.target.contains(search))
                .or(manageArticle.responsibility.contains(search)),
                manageArticle.office.id.eq(userOfficeId),
                manageArticle.deletedAt.isNull()
            ).fetch();
    }
}


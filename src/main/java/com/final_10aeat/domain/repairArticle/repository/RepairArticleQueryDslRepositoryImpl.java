package com.final_10aeat.domain.repairArticle.repository;

import com.final_10aeat.domain.repairArticle.entity.QRepairArticle;
import com.final_10aeat.domain.repairArticle.entity.RepairArticle;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class RepairArticleQueryDslRepositoryImpl implements RepairArticleQueryDslRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<RepairArticle> searchByText(Long userOfficeId, String search) {
        QRepairArticle repairArticle = QRepairArticle.repairArticle;
//삭제된거 가져오면 안댐
        return queryFactory.selectFrom(repairArticle)
            .where(
                    repairArticle.office.id.eq(userOfficeId),
                    repairArticle.title.contains(search)
                    .or(repairArticle.content.contains(search)),
                    repairArticle.deletedAt.isNull()
            )
            .fetch();
    }
}

package com.final_10aeat.domain.repairArticle.repository;

import com.final_10aeat.domain.repairArticle.entity.QRepairArticle;
import com.final_10aeat.domain.repairArticle.entity.RepairArticle;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class RepairArticleQueryDslRepositoryImpl implements RepairArticleQueryDslRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<RepairArticle> searchByTextAnsOfficeId(
        Long userOfficeId, String search, Pageable pageRequest
    ) {
        QRepairArticle repairArticle = QRepairArticle.repairArticle;

        JPAQuery<RepairArticle> query = queryFactory.selectFrom(repairArticle)
            .where(
                repairArticle.office.id.eq(userOfficeId),
                repairArticle.title.contains(search)
                    .or(repairArticle.content.contains(search)),
                repairArticle.deletedAt.isNull()
            )
            .offset(pageRequest.getOffset())
            .limit(pageRequest.getPageSize());

        for (Sort.Order order : pageRequest.getSort()) {
            query.orderBy(
                new OrderSpecifier(order.isAscending() ? Order.ASC : Order.DESC,
                    new PathBuilder<>(
                        repairArticle.getType(), repairArticle.getMetadata()
                    ).get(order.getProperty())));
        }
        QueryResults<RepairArticle> queryResult = query.fetchResults();

        return new PageImpl<>(queryResult.getResults(), pageRequest, queryResult.getTotal());
    }
}

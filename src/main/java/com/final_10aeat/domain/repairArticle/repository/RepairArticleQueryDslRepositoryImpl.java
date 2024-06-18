package com.final_10aeat.domain.repairArticle.repository;

import static java.util.Optional.ofNullable;

import com.final_10aeat.common.enumclass.ArticleCategory;
import com.final_10aeat.common.enumclass.Progress;
import com.final_10aeat.domain.articleIssue.entity.QArticleIssue;
import com.final_10aeat.domain.repairArticle.dto.request.SearchRepairArticleQuery;
import com.final_10aeat.domain.repairArticle.entity.QRepairArticle;
import com.final_10aeat.domain.repairArticle.entity.RepairArticle;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
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

    @Override
    public List<RepairArticle> findSoftDeletedBefore(LocalDateTime cutoffDate) {
        QRepairArticle repairArticle = QRepairArticle.repairArticle;

        return queryFactory.selectFrom(repairArticle)
            .where(repairArticle.deletedAt.isNotNull()
                .and(repairArticle.deletedAt.before(cutoffDate)))
            .fetch();
    }

    @Override
    public Page<RepairArticle> findByOfficeIdAndProgressInAndCategoryOrderByIdDesc(
        Long officeId, List<Progress> progresses, ArticleCategory category, Pageable pageable,
        List<Long> checkedIssueIds
    ) {
        QRepairArticle repairArticle = QRepairArticle.repairArticle;
        QArticleIssue articleIssue = QArticleIssue.articleIssue;

        BooleanExpression progressPredicate =
            progresses != null ? repairArticle.progress.in(progresses) : null;
        BooleanExpression categoryPredicate =
            category != null ? repairArticle.category.eq(category) : null;

        List<RepairArticle> redDotArticles = queryFactory.selectFrom(repairArticle)
            .leftJoin(repairArticle.issues, articleIssue)
            .where(repairArticle.office.id.eq(officeId),
                progressPredicate,
                categoryPredicate,
                articleIssue.enabled.isTrue(),
                articleIssue.id.notIn(checkedIssueIds))
            .distinct()
            .orderBy(repairArticle.id.desc())
            .fetch();

        List<RepairArticle> nonRedDotArticles = queryFactory.selectFrom(repairArticle)
            .leftJoin(repairArticle.issues, articleIssue)
            .where(repairArticle.office.id.eq(officeId),
                progressPredicate,
                categoryPredicate,
                articleIssue.isNull()
                    .or(articleIssue.enabled.isFalse().or(articleIssue.id.in(checkedIssueIds))))
            .distinct()
            .orderBy(repairArticle.id.desc())
            .fetch();

        List<RepairArticle> combinedArticles = Stream.concat(redDotArticles.stream(),
                nonRedDotArticles.stream())
            .distinct()
            .collect(Collectors.toList());

        long total = combinedArticles.size();

        int start = Math.min((int) pageable.getOffset(), combinedArticles.size());
        int end = Math.min((start + pageable.getPageSize()), combinedArticles.size());
        List<RepairArticle> paginatedArticles = combinedArticles.subList(start, end);

        return new PageImpl<>(paginatedArticles, pageable, total);
    }

    @Override
    public List<RepairArticle> findByOfficeIdAndProgressIn(Long officeId,
        List<Progress> progresses) {
        QRepairArticle repairArticle = QRepairArticle.repairArticle;

        return queryFactory.selectFrom(repairArticle)
            .where(repairArticle.office.id.eq(officeId),
                repairArticle.progress.in(progresses))
            .fetch();
    }

    @Override
    public Page<RepairArticle> findAll(SearchRepairArticleQuery command) {
        QRepairArticle repairArticle = QRepairArticle.repairArticle;
        Pageable pageRequest = command.pageRequest();

        JPAQuery<RepairArticle> query = queryFactory.selectFrom(repairArticle)
            .where(setQuery(command, repairArticle))
            .orderBy(repairArticle.id.desc())
            .offset(pageRequest.getOffset())
            .limit(pageRequest.getPageSize());

        QueryResults<RepairArticle> queryResult = query.fetchResults();

        return new PageImpl<>(queryResult.getResults(), pageRequest, queryResult.getTotal());
    }

    private BooleanBuilder setQuery(SearchRepairArticleQuery command,
        QRepairArticle repairArticle) {
        BooleanBuilder booleanBuilder = new BooleanBuilder();

        booleanBuilder.and(repairArticle.office.id.eq(command.officeId()));
        booleanBuilder.and(repairArticle.deletedAt.isNull());

        if (ofNullable(command.progress()).isPresent()) {
            booleanBuilder.and(repairArticle.progress.eq(command.progress()));
        }

        if (ofNullable(command.keyword()).isPresent()) {
            booleanBuilder.and(repairArticle.title.contains(command.keyword()));
        }

        return booleanBuilder;
    }
}

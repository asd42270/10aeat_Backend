package com.final_10aeat.domain.manageArticle.repository;

import com.final_10aeat.common.enumclass.Progress;
import com.final_10aeat.domain.manageArticle.dto.request.GetMonthlyListWithYearQuery;
import com.final_10aeat.domain.manageArticle.dto.request.GetYearListQuery;
import com.final_10aeat.domain.manageArticle.entity.ManageArticle;
import com.final_10aeat.domain.manageArticle.entity.QManageArticle;
import com.final_10aeat.domain.manageArticle.entity.QManageSchedule;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ManageArticleQueryDslRepositoryImpl implements ManageArticleQueryDslRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<ManageArticle> searchByOfficeIdAndText(
        Long userOfficeId, String search, Pageable pageRequest
    ) {
        QManageArticle manageArticle = QManageArticle.manageArticle;

        JPAQuery<ManageArticle> query = queryFactory.selectFrom(manageArticle)
            .where(manageArticle.title.contains(search)
                    .or(manageArticle.legalBasis.contains(search))
                    .or(manageArticle.target.contains(search))
                    .or(manageArticle.responsibility.contains(search)),
                manageArticle.office.id.eq(userOfficeId),
                manageArticle.deletedAt.isNull()
            )
            .offset(pageRequest.getOffset())
            .limit(pageRequest.getPageSize());

        return sortAndFetchToPage(pageRequest, manageArticle, query);
    }

    @Override
    public Page<ManageArticle> findAllByUnDeletedOfficeIdAndScheduleYearAndProgress(
        Long userOfficeId, Integer year, Pageable pageRequest, List<Progress> progresses
    ) {
        QManageArticle manageArticle = QManageArticle.manageArticle;
        QManageSchedule manageSchedule = QManageSchedule.manageSchedule;

        JPAQuery<ManageArticle> query = queryFactory.selectFrom(manageArticle)
            .leftJoin(manageArticle.schedules, manageSchedule)
            .where(
                manageSchedule.year.eq(year),
                manageArticle.office.id.eq(userOfficeId),
                manageArticle.deletedAt.isNull(),
                manageArticle.progress.in(progresses)
            )
            .offset(pageRequest.getOffset())
            .limit(pageRequest.getPageSize());

        return sortAndFetchToPage(pageRequest, manageArticle, query);
    }

    @Override
    public Page<ManageArticle> findAllByYear(GetYearListQuery command) {
        QManageArticle manageArticle = QManageArticle.manageArticle;
        QManageSchedule manageSchedule = QManageSchedule.manageSchedule;
        Pageable pageRequest = command.pageRequest();

        JPAQuery<ManageArticle> query = queryFactory.selectFrom(manageArticle)
            .leftJoin(manageArticle.schedules, manageSchedule)
            .where(
                manageSchedule.year.eq(command.year()),
                manageArticle.office.id.eq(command.officeId()),
                manageArticle.deletedAt.isNull()
            )
            .offset(pageRequest.getOffset())
            .limit(pageRequest.getPageSize());

        return sortAndFetchToPage(pageRequest, manageArticle, query);
    }

    @Override
    public Page<ManageArticle> findAllByYearAndMonthly(GetMonthlyListWithYearQuery command) {
        QManageArticle manageArticle = QManageArticle.manageArticle;
        QManageSchedule manageSchedule = QManageSchedule.manageSchedule;
        Pageable pageRequest = command.pageRequest();

        JPAQuery<ManageArticle> query = queryFactory.selectFrom(manageArticle)
            .leftJoin(manageArticle.schedules, manageSchedule)
            .where(
                manageSchedule.year.eq(command.year()),
                manageSchedule.month.eq(command.month()),
                manageArticle.office.id.eq(command.officeId()),
                manageArticle.deletedAt.isNull()
            )
            .offset(pageRequest.getOffset())
            .limit(pageRequest.getPageSize());

        return sortAndFetchToPage(pageRequest, manageArticle, query);
    }

    @NotNull
    private Page<ManageArticle> sortAndFetchToPage(
        Pageable pageRequest, QManageArticle manageArticle, JPAQuery<ManageArticle> query
    ) {
        for (Sort.Order order : pageRequest.getSort()) {
            query.orderBy(
                new OrderSpecifier(order.isAscending() ? Order.ASC : Order.DESC,
                    new PathBuilder<>(
                        manageArticle.getType(), manageArticle.getMetadata()
                    ).get(order.getProperty())));
        }

        QueryResults<ManageArticle> queryResult = query.fetchResults();

        return new PageImpl<>(queryResult.getResults(), pageRequest, queryResult.getTotal());
    }
}


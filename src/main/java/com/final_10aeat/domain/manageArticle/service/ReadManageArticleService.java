package com.final_10aeat.domain.manageArticle.service;

import static com.final_10aeat.domain.manageArticle.dto.request.GetYearListPageQuery.toQueryDto;
import static java.util.Optional.ofNullable;

import com.final_10aeat.common.enumclass.Progress;
import com.final_10aeat.common.exception.ArticleNotFoundException;
import com.final_10aeat.common.exception.UnauthorizedAccessException;
import com.final_10aeat.domain.articleIssue.entity.ArticleIssue;
import com.final_10aeat.domain.manageArticle.dto.request.GetMonthlyListByYearQuery;
import com.final_10aeat.domain.manageArticle.dto.request.GetYearListQuery;
import com.final_10aeat.domain.manageArticle.dto.request.SearchManageArticleQuery;
import com.final_10aeat.domain.manageArticle.dto.response.ManageArticleDetailResponseDto;
import com.final_10aeat.domain.manageArticle.dto.response.ManageArticleListResponseDto;
import com.final_10aeat.domain.manageArticle.dto.response.ManageArticleSummaryResponseDto;
import com.final_10aeat.domain.manageArticle.dto.response.SearchManagerManageArticleResponseDto;
import com.final_10aeat.domain.manageArticle.dto.response.ManageArticleSummaryListResponseDto;
import com.final_10aeat.domain.manageArticle.dto.util.ScheduleConverter;
import com.final_10aeat.domain.manageArticle.entity.ManageArticle;
import com.final_10aeat.domain.manageArticle.entity.ManageSchedule;
import com.final_10aeat.domain.manageArticle.repository.ManageArticleRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReadManageArticleService {

    private final ManageArticleRepository manageArticleRepository;

    private final List<Progress> unCompleteProgress = List.of(
        Progress.PENDING,
        Progress.INPROGRESS
    );

    private final List<Progress> completeProgress = List.of(
        Progress.COMPLETE
    );

    public ManageArticleSummaryListResponseDto summary(Long id, Integer year) {
        List<ManageArticle> articles = manageArticleRepository
            .findAllByYear(GetYearListQuery.toQueryDto(year, id));

        return summaryArticleFrom(articles);
    }

    private ManageArticleSummaryListResponseDto summaryArticleFrom(List<ManageArticle> articles) {
        ArrayList<Long> issueList = new ArrayList<>();
        int complete = 0;
        int inprogress = 0;
        int pending = 0;

        for (ManageArticle article : articles) {
            Progress progress = article.getProgress();
            if (progress == Progress.COMPLETE) {
                complete++;
            }
            if (progress == Progress.INPROGRESS) {
                inprogress++;
            }
            if (progress == Progress.PENDING) {
                pending++;
            }
            article.getActiveIssue().ifPresent(issue -> issueList.add(issue.getId()));
        }

        return new ManageArticleSummaryListResponseDto(complete, inprogress, pending, issueList);
    }

    public ManageArticleDetailResponseDto detailArticle(Long userOfficeId, Long articleId) {
        ManageArticle article = manageArticleRepository.findByIdAndDeletedAtNull(
                articleId)
            .orElseThrow(ArticleNotFoundException::new);

        if (!article.getOffice().getId().equals(userOfficeId)) {
            throw new UnauthorizedAccessException();
        }

        return detailArticleFrom(article);
    }

    private ManageArticleDetailResponseDto detailArticleFrom(ManageArticle article) {
        return ManageArticleDetailResponseDto.builder()
            .period(article.getPeriod())
            .periodCount(article.getPeriodCount())
            .title(article.getTitle())
            .issueId(article.getActiveIssue().map(ArticleIssue::getId).orElse(null))
            .progress(article.getProgress())
            .legalBasis(article.getLegalBasis())
            .target(article.getTarget())
            .responsibility(article.getResponsibility())
            .note(article.getResponsibility())
            .manageSchedule(
                article.getSchedules().stream()
                    .sorted(Comparator.comparing(ManageSchedule::getScheduleStart).reversed())
                    .map(ScheduleConverter::toScheduleResponse).toList()
            )
            .build();
    }

    public Page<ManageArticleListResponseDto> listArticleByProgress(
        Integer year, Long userOfficeId, Pageable pageRequest
    ) {
        return manageArticleRepository
            .searchByKeyword(SearchManageArticleQuery.builder()
                .year(year)
                .officeId(userOfficeId)
                .pageRequest(pageRequest)
                .build())
            .map(this::listArticleFrom);
    }

    public Page<ManageArticleListResponseDto> listArticleByProgress(
        Integer year, Long userOfficeId, Pageable pageRequest, Boolean complete
    ) {
        Page<ManageArticle> articles = manageArticleRepository
            .findAllByUnDeletedOfficeIdAndScheduleYearAndProgress(
                userOfficeId, year, pageRequest, complete ? completeProgress : unCompleteProgress
            );

        return articles.map(this::listArticleFrom);
    }

    private ManageArticleListResponseDto listArticleFrom(ManageArticle article) {
        return ManageArticleListResponseDto.builder()
            .id(article.getId())
            .period(article.getPeriod())
            .periodCount(article.getPeriodCount())
            .title(article.getTitle())
            .allSchedule(article.getSchedules().size())
            .completedSchedule(
                (int) article.getSchedules().stream()
                    .filter(ManageSchedule::isComplete).count()
            )
            .issueId(article.getActiveIssue().map(ArticleIssue::getId).orElse(null))
            .build();
    }

    public List<ManageArticleSummaryResponseDto> monthlySummary(Integer year, Long officeId) {
        List<ManageArticle> articles = manageArticleRepository.findAllByOfficeIdAndDeletedAtNull(
            officeId);

        return toSummaryDto(articles, year);
    }

    private List<ManageArticleSummaryResponseDto> toSummaryDto(List<ManageArticle> articles,
        Integer year) {
        List<ManageArticleSummaryResponseDto> monthly = new ArrayList<>();
        HashMap<Integer, Set<Long>> monthArticleIdMap = new HashMap<>();

        articles.forEach(
            article -> article.getSchedules().stream()
                .filter(schedule -> schedule.getYear().equals(year))
                .forEach(schedule -> {
                    Integer month = schedule.getMonth();
                    monthArticleIdMap.computeIfAbsent(month, k -> new HashSet<>())
                        .add(article.getId());
                })
        );

        monthArticleIdMap.forEach(
            (key, value) -> monthly.add(new ManageArticleSummaryResponseDto(key, value.size()))
        );
        return monthly;
    }

    public Page<ManageArticleListResponseDto> monthlyListArticle(
        Long userOfficeId, Integer year, Integer month, Pageable pageRequest
    ) {
        if (ofNullable(month).isEmpty()) {
            return manageArticleRepository
                .findAllByYear(toQueryDto(year, userOfficeId, pageRequest))
                .map(this::listArticleFrom);
        }

        return manageArticleRepository
            .findAllByYearAndMonthly(GetMonthlyListByYearQuery
                .toQueryDto(year, month, userOfficeId, pageRequest)
            )
            .map(this::listArticleFrom);
    }

    public Page<ManageArticleListResponseDto> search(
        Long userOfficeId, String search, Pageable pageRequest
    ) {
        return manageArticleRepository.searchByOfficeIdAndText(userOfficeId, search, pageRequest)
            .map(this::listArticleFrom);
    }

    public Page<SearchManagerManageArticleResponseDto> managerSearch(
        Long userOfficeId, Integer year, String keyword, Integer month, Pageable pageRequest,
        LocalDateTime now) {
        return manageArticleRepository.searchByKeyword(
                SearchManageArticleQuery.builder()
                    .now(now)
                    .keyword(keyword)
                    .year(year)
                    .month(month)
                    .officeId(userOfficeId)
                    .pageRequest(pageRequest)
                    .build()
            )
            .map(this::searchArticleFrom);
    }

    private SearchManagerManageArticleResponseDto searchArticleFrom(ManageArticle article) {
        return SearchManagerManageArticleResponseDto.builder()
            .id(article.getId())
            .period(article.getPeriod())
            .periodCount(article.getPeriodCount())
            .title(article.getTitle())
            .allSchedule(article.getSchedules().size())
            .completedSchedule(
                (int) article.getSchedules().stream()
                    .filter(ManageSchedule::isComplete).count()
            )
            .currentSchedules(getCurrentSchedules(article))
            .build();
    }

    private LocalDateTime getCurrentSchedules(ManageArticle article) {
        LocalDateTime now = LocalDateTime.now();
        return article.getSchedules().stream()
            .map(ManageSchedule::getScheduleStart)
            .filter(start -> !start.isBefore(now))
            .min(Comparator.naturalOrder())
            .orElse(null);
    }
}

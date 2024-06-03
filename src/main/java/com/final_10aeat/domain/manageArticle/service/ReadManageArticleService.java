package com.final_10aeat.domain.manageArticle.service;

import static java.util.Optional.ofNullable;

import com.final_10aeat.common.enumclass.Progress;
import com.final_10aeat.common.exception.ArticleNotFoundException;
import com.final_10aeat.common.exception.UnauthorizedAccessException;
import com.final_10aeat.domain.articleIssue.entity.ArticleIssue;
import com.final_10aeat.domain.manageArticle.dto.response.DetailManageArticleResponse;
import com.final_10aeat.domain.manageArticle.dto.response.ListManageArticleResponse;
import com.final_10aeat.domain.manageArticle.dto.response.SummaryManageArticleResponse;
import com.final_10aeat.domain.manageArticle.dto.util.ScheduleConverter;
import com.final_10aeat.domain.manageArticle.entity.ManageArticle;
import com.final_10aeat.domain.manageArticle.entity.ManageSchedule;
import com.final_10aeat.domain.manageArticle.repository.ManageArticleRepository;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReadManageArticleService {

    private final ManageArticleRepository manageArticleRepository;

    public SummaryManageArticleResponse summary(Long id) {
        List<ManageArticle> articles = manageArticleRepository
            .findAllByOfficeIdAndDeletedAtNull(id);

        return summaryArticleFrom(articles);
    }

    private SummaryManageArticleResponse summaryArticleFrom(List<ManageArticle> articles) {
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

            if (ofNullable(article.getIssue()).isPresent()) {
                issueList.add(article.getIssue().getId());
            }
        }

        return new SummaryManageArticleResponse(complete, inprogress, pending, issueList);
    }

    public DetailManageArticleResponse detailArticle(Long userOfficeId, Long articleId) {
        ManageArticle article = manageArticleRepository.findByIdAndDeletedAtNull(
                articleId)
            .orElseThrow(ArticleNotFoundException::new);

        if (!article.getOffice().getId().equals(userOfficeId)) {
            throw new UnauthorizedAccessException();
        }

        return detailArticleFrom(article);
    }

    private DetailManageArticleResponse detailArticleFrom(ManageArticle article) {
        return DetailManageArticleResponse.builder()
            .period(article.getPeriod())
            .periodCount(article.getPeriodCount())
            .title(article.getTitle())
            .issueId(article.getIssue().getId())
            .progress(article.getProgress())
            .legalBasis(article.getLegalBasis())
            .target(article.getTarget())
            .responsibility(article.getResponsibility())
            .note(article.getResponsibility())
            .manageSchedule(
                article.getSchedules().stream()
                    .map(ScheduleConverter::toScheduleResponse).toList()
            )
            .build();
    }

    public List<ListManageArticleResponse> listArticle(Integer year, Long userOfficeId) {
        List<ManageArticle> articles = manageArticleRepository
            .findAllByOfficeIdAndDeletedAtNull(userOfficeId);

        return articles.stream()
            .filter(
                article -> article.getSchedules().stream()
                    .anyMatch(
                        schedule -> schedule.getYear().equals(year)
                    )
            )
            .map(this::listArticleFrom).toList();
    }

    private ListManageArticleResponse listArticleFrom(ManageArticle article) {
        return ListManageArticleResponse.builder()
            .id(article.getId())
            .period(article.getPeriod())
            .periodCount(article.getPeriodCount())
            .title(article.getTitle())
            .allSchedule(article.getSchedules().size())
            .completedSchedule(
                (int) article.getSchedules().stream()
                    .filter(ManageSchedule::isComplete).count()
            )
            .issueId(ofNullable(article.getIssue())
                .map(ArticleIssue::getId).orElse(0L)
            )
            .build();
    }

    public Set<Integer> monthlySummary(Integer year, Long officeId) {
        Set<Integer> monthly = new HashSet<>();
        List<ManageArticle> articles = manageArticleRepository
            .findAllByOfficeIdAndDeletedAtNull(officeId);

        articles.forEach(
            article -> article.getSchedules()
                .stream().filter(
                    manageSchedule -> manageSchedule.getYear().equals(year)
                ).forEach(
                    schedule -> monthly.add(schedule.getMonth())
                )
        );
        return monthly;
    }

    public List<ListManageArticleResponse> monthlyListArticle(
        Long userOfficeId, Integer year, Integer month
    ) {
        List<ManageArticle> articles = manageArticleRepository
            .findAllByOfficeIdAndDeletedAtNull(userOfficeId);

        if (ofNullable(month).isEmpty()) {
            return listArticleMonthlyNullFrom(articles, year);
        }

        return listArticleMonthlyFrom(articles, year, month);
    }

    private List<ListManageArticleResponse> listArticleMonthlyFrom(
        List<ManageArticle> articles,
        Integer year, Integer month
    ) {
        return articles.stream()
            .filter(
                article -> article.getSchedules().stream()
                    .anyMatch(schedule ->
                        schedule.getYear().equals(year) && schedule.getMonth().equals(month)
                    )
            )
            .map(this::listArticleFrom)
            .toList();
    }

    private List<ListManageArticleResponse> listArticleMonthlyNullFrom(
        List<ManageArticle> articles,
        Integer year
    ) {
        return articles.stream()
            .filter(
                article -> article.getSchedules().stream()
                    .anyMatch(schedule -> schedule.getYear().equals(year))
            )
            .map(this::listArticleFrom)
            .toList();
    }
}

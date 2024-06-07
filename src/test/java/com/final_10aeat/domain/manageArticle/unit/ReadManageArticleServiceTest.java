package com.final_10aeat.domain.manageArticle.unit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

import com.final_10aeat.common.enumclass.ManagePeriod;
import com.final_10aeat.common.enumclass.Progress;
import com.final_10aeat.common.exception.ArticleNotFoundException;
import com.final_10aeat.common.exception.UnauthorizedAccessException;
import com.final_10aeat.common.util.EntityUtil;
import com.final_10aeat.domain.articleIssue.entity.ArticleIssue;
import com.final_10aeat.domain.manageArticle.dto.response.DetailManageArticleResponse;
import com.final_10aeat.domain.manageArticle.dto.response.ListManageArticleResponse;
import com.final_10aeat.domain.manageArticle.dto.response.SummaryManageArticleResponse;
import com.final_10aeat.domain.manageArticle.entity.ManageArticle;
import com.final_10aeat.domain.manageArticle.entity.ManageSchedule;
import com.final_10aeat.domain.manageArticle.repository.ManageArticleRepository;
import com.final_10aeat.domain.manageArticle.service.ReadManageArticleService;
import com.final_10aeat.domain.office.entity.Office;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class ReadManageArticleServiceTest {

    @Mock
    private ManageArticleRepository manageArticleRepository;

    @InjectMocks
    private ReadManageArticleService readManageArticleService;

    private final Office office = EntityUtil.DEFAULT_OFFICE;
    private final ManageSchedule schedule1 = ManageSchedule.builder()
        .month(1).year(2024)
        .complete(true)
        .build();
    private final ManageSchedule schedule2 = ManageSchedule.builder()
        .month(1).year(2023)
        .complete(false)
        .build();
    private final ManageSchedule schedule3 = ManageSchedule.builder()
        .month(2).year(2023)
        .complete(false)
        .build();
    private final ManageSchedule schedule4 = ManageSchedule.builder()
        .month(2).year(2024)
        .complete(true)
        .build();
    private final ArticleIssue issue = ArticleIssue.builder()
        .id(1L)
        .enabled(true)
        .build();
    private final ManageArticle article1 = ManageArticle.builder()
        .id(1L)
        .period(ManagePeriod.YEAR)
        .progress(Progress.COMPLETE)
        .office(office)
        .schedules(new ArrayList<>())
        .issues(new HashSet<>())
        .build();

    private final ManageArticle article2 = ManageArticle.builder()
        .id(2L)
        .period(ManagePeriod.MONTH)
        .progress(Progress.PENDING)
        .office(office)
        .schedules(new ArrayList<>())
        .issues(new HashSet<>())
        .build();


    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("summary() 에 성공한다")
    void summary_WillSuccess() {
        // given
        article1.addSchedule(schedule1);
        article2.addSchedule(schedule3);
        article1.getIssues().add(issue);
        List<ManageArticle> articleList = List.of(article1, article2);
        given(manageArticleRepository.findAllByOfficeIdAndDeletedAtNull(anyLong()))
            .willReturn(articleList);

        // when
        SummaryManageArticleResponse summary = readManageArticleService.summary(1L);

        // then
        assertThat(summary.complete()).isEqualTo(1);
        assertThat(summary.hasIssue().size()).isEqualTo(1);
    }

    @Nested
    @DisplayName("detailArticle()는 ")
    class Context_DetailArticle {

        @Test
        @DisplayName("성공한다.")
        void _WillSuccess() {
            // given
            article1.addSchedule(schedule1);
            given(manageArticleRepository.findByIdAndDeletedAtNull(anyLong()))
                .willReturn(Optional.of(article1));

            // when
            DetailManageArticleResponse result = readManageArticleService
                .detailArticle(1L, 1L);

            // then
            assertThat(result.manageSchedule().size()).isEqualTo(1);
        }

        @Test
        @DisplayName("권한이 없는 게시글에 요청을 보내 실패한다.")
        void ArticleUnauthorized_willFall() {
            // given
            article1.addSchedule(schedule1);
            given(manageArticleRepository.findByIdAndDeletedAtNull(anyLong()))
                .willReturn(Optional.of(article1));

            // when & then
            assertThrows(UnauthorizedAccessException.class,
                () -> readManageArticleService.detailArticle(
                    2L, 1L));
        }

        @Test
        @DisplayName("존재하지 않는 게시글에 요청을 보내 실패한다.")
        void ArticleNotFound_willFall() {
            // given
            given(manageArticleRepository.findByIdAndDeletedAtNull(anyLong()))
                .willReturn(Optional.empty());

            // when & then
            assertThrows(ArticleNotFoundException.class,
                () -> readManageArticleService.detailArticle(
                    1L, 1L));
        }
    }

    @Test
    @DisplayName("listArticle() 에 성공한다")
    void listArticle_WillSuccess() {
        // given
        article1.addSchedule(schedule1);
        article1.addSchedule(schedule3);
        article1.addSchedule(schedule4);
        article2.addSchedule(schedule2);

        List<ManageArticle> articleList = List.of(article1, article2);

        given(manageArticleRepository.findAllByOfficeIdAndDeletedAtNull(anyLong()))
            .willReturn(articleList);

        // when
        List<ListManageArticleResponse> result =
            readManageArticleService.listArticle(2024, 1L);

        // then
        assertThat(result.size()).isEqualTo(1);
        assertThat(result.get(0).allSchedule()).isEqualTo(3);
        assertThat(result.get(0).completedSchedule()).isEqualTo(2);
    }

    @Test
    @DisplayName("monthlySummary() 에 성공한다")
    void monthlySummary_WillSuccess() {
        // given
        article1.addSchedule(schedule1);
        article1.addSchedule(schedule3);
        article1.addSchedule(schedule4);
        article2.addSchedule(schedule2);

        List<ManageArticle> articleList = List.of(article1, article2);

        given(manageArticleRepository.findAllByOfficeIdAndDeletedAtNull(anyLong()))
            .willReturn(articleList);

        // when
        var result =
            readManageArticleService.monthlySummary(2024, 1L);

        // then
        assertThat(result.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("monthlyListArticle() 에 성공한다")
    void monthlyListArticle_WillSuccess() {
        // given
        article1.addSchedule(schedule1);
        article1.addSchedule(schedule3);
        article1.addSchedule(schedule4);
        article2.addSchedule(schedule2);

        List<ManageArticle> articleList = List.of(article1, article2);

        given(manageArticleRepository.findAllByOfficeIdAndDeletedAtNull(anyLong()))
            .willReturn(articleList);

        // when
        var result =
            readManageArticleService.monthlyListArticle(1L, 2024, 1);

        // then
        assertThat(result.size()).isEqualTo(1);
    }
}

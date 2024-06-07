package com.final_10aeat.domain.issue.unit;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;

import com.final_10aeat.domain.articleIssue.dto.response.ArticleIssueCheckResponseDto;
import com.final_10aeat.domain.articleIssue.entity.ArticleIssue;
import com.final_10aeat.domain.articleIssue.exception.InactiveIssueException;
import com.final_10aeat.domain.articleIssue.exception.IssueNotFoundException;
import com.final_10aeat.domain.articleIssue.repository.ArticleIssueRepository;
import com.final_10aeat.domain.articleIssue.service.ArticleIssueCheckService;
import com.final_10aeat.domain.repairArticle.entity.RepairArticle;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class RepairIssueDetailServiceTest {

    @Mock
    private ArticleIssueRepository articleIssueRepository;

    @InjectMocks
    private ArticleIssueCheckService articleIssueCheckService;

    private final RepairArticle repairArticle = RepairArticle.builder()
        .id(1L)
        .title("게시글")
        .content("게시글 내용")
        .build();

    private final ArticleIssue articleIssue = ArticleIssue.builder()
        .id(1L)
        .title("이슈")
        .content("내용")
        .repairArticle(repairArticle)
        .enabled(true)
        .build();

    private final ArticleIssueCheckResponseDto responseDto = ArticleIssueCheckResponseDto.builder()
        .title(articleIssue.getTitle())
        .content(articleIssue.getContent())
        .build();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Nested
    @DisplayName("getIssueDetail()은")
    class Context_GetIssueDetail {

        @Test
        @DisplayName("이슈 조회에 성공한다.")
        void _willSuccess() {
            // given
            Long issueId = 1L;
            given(articleIssueRepository.findById(issueId)).willReturn(Optional.of(articleIssue));

            // when & then
            Assertions.assertThat(articleIssueCheckService.getIssueDetail(issueId).title())
                .isEqualTo(responseDto.title());
        }

        @Test
        @DisplayName("이슈가 비활성화 상태라 조회에 실패한다.")
        void _inactiveIssue() {
            // given
            Long issueId = 1L;
            articleIssue.setEnabled(false);
            given(articleIssueRepository.findById(issueId)).willReturn(Optional.of(articleIssue));

            // when & then
            assertThrows(InactiveIssueException.class,
                () -> articleIssueCheckService.getIssueDetail(issueId));
        }

        @Test
        @DisplayName("이슈가 존재하지 않아 조회에 실패한다.")
        void _issueNotFound() {
            // given
            Long issueId = 1L;
            given(articleIssueRepository.findById(issueId)).willReturn(Optional.empty());

            // when & then
            assertThrows(IssueNotFoundException.class,
                () -> articleIssueCheckService.getIssueDetail(issueId));
        }
    }
}

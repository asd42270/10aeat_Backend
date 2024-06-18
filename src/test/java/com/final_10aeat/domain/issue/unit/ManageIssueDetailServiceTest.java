package com.final_10aeat.domain.issue.unit;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;

import com.final_10aeat.domain.articleIssue.dto.response.IssueDetailResponseDto;
import com.final_10aeat.domain.articleIssue.entity.ArticleIssue;
import com.final_10aeat.domain.articleIssue.exception.DisabledIssueException;
import com.final_10aeat.domain.articleIssue.exception.IssueNotFoundException;
import com.final_10aeat.domain.articleIssue.repository.ArticleIssueRepository;
import com.final_10aeat.domain.articleIssue.service.IssueCheckService;
import com.final_10aeat.domain.manageArticle.entity.ManageArticle;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class ManageIssueDetailServiceTest {

    @Mock
    private ArticleIssueRepository articleIssueRepository;

    @InjectMocks
    private IssueCheckService issueCheckService;

    private final ManageArticle manageArticle = ManageArticle.builder()
        .id(1L)
        .title("게시글")
        .note("게시글 내용")
        .build();

    private final ArticleIssue articleIssue = ArticleIssue.builder()
        .id(1L)
        .title("이슈")
        .content("내용")
        .manageArticle(manageArticle)
        .enabled(true)
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
            Long issueId = articleIssue.getId();
            given(articleIssueRepository.findById(issueId)).willReturn(Optional.of(articleIssue));

            // when
            IssueDetailResponseDto responseDto = issueCheckService.getIssueDetail(
                issueId);

            // then
            Assertions.assertThat(responseDto.title()).isEqualTo(articleIssue.getTitle());
        }

        @Test
        @DisplayName("이슈가 없어 조회에 실패한다.")
        void _issueNotFound() {
            // given
            Long issueId = articleIssue.getId();
            given(articleIssueRepository.findById(issueId)).willReturn(Optional.empty());

            // when & then
            assertThrows(IssueNotFoundException.class,
                () -> issueCheckService.getIssueDetail(issueId));
        }

        @Test
        @DisplayName("이슈가 비활성화 상태라 조회에 실패한다.")
        void _inactiveIssue() {
            // given
            Long issueId = articleIssue.getId();
            articleIssue.setEnabled(false);
            given(articleIssueRepository.findById(issueId)).willReturn(Optional.of(articleIssue));

            // when & then
            assertThrows(DisabledIssueException.class,
                () -> issueCheckService.getIssueDetail(issueId));
        }
    }
}

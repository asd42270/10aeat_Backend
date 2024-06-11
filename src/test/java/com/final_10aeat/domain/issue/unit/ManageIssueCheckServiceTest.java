package com.final_10aeat.domain.issue.unit;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import com.final_10aeat.common.enumclass.MemberRole;
import com.final_10aeat.domain.articleIssue.dto.request.ArticleIssueCheckRequestDto;
import com.final_10aeat.domain.articleIssue.entity.ArticleIssue;
import com.final_10aeat.domain.articleIssue.entity.ArticleIssueCheck;
import com.final_10aeat.domain.articleIssue.exception.InactiveIssueException;
import com.final_10aeat.domain.articleIssue.exception.IssueNotFoundException;
import com.final_10aeat.domain.articleIssue.repository.ArticleIssueCheckRepository;
import com.final_10aeat.domain.articleIssue.repository.ArticleIssueRepository;
import com.final_10aeat.domain.articleIssue.service.ArticleIssueCheckService;
import com.final_10aeat.domain.manageArticle.entity.ManageArticle;
import com.final_10aeat.domain.manageArticle.repository.ManageArticleRepository;
import com.final_10aeat.domain.member.entity.Member;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class ManageIssueCheckServiceTest {

    @Mock
    private ManageArticleRepository manageArticleRepository;
    @Mock
    private ArticleIssueCheckRepository articleIssueCheckRepository;
    @Mock
    private ArticleIssueRepository articleIssueRepository;

    @InjectMocks
    private ArticleIssueCheckService articleIssueCheckService;

    private final Member member = Member.builder()
        .id(1L)
        .email("test@test.com")
        .password("encodedPassword")
        .name("spring")
        .role(MemberRole.OWNER)
        .build();

    private final ManageArticle manageArticle = ManageArticle.builder()
        .id(1L)
        .title("유지관리 게시글")
        .note("유지관리 게시글 내용")
        .build();

    private final ArticleIssue articleIssue = ArticleIssue.builder()
        .id(1L)
        .title("이슈가 발행됐어요")
        .content("이슈에요")
        .manageArticle(manageArticle)
        .enabled(true)
        .build();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Nested
    @DisplayName("manageIssueCheck()는")
    class Context_manageIssueCheck {

        @Test
        @DisplayName("이슈 확인에 성공한다.")
        void _willSuccess() {
            // given
            ArticleIssueCheckRequestDto requestDto = new ArticleIssueCheckRequestDto(true);
            given(articleIssueRepository.findById(articleIssue.getId())).willReturn(
                Optional.of(articleIssue));

            // when
            articleIssueCheckService.checkIssue(requestDto, articleIssue.getId(), member);

            // then
            verify(articleIssueRepository).findById(articleIssue.getId());
            verify(articleIssueCheckRepository).save(any(ArticleIssueCheck.class));
        }

        @Test
        @DisplayName("게시글이 없어 확인에 실패한다.")
        void _articleNotFound() {
            // given
            ArticleIssueCheckRequestDto requestDto = new ArticleIssueCheckRequestDto(true);
            given(articleIssueRepository.findById(articleIssue.getId())).willReturn(
                Optional.empty());

            // when&then
            assertThrows(IssueNotFoundException.class,
                () -> articleIssueCheckService.checkIssue(requestDto, articleIssue.getId(),
                    member));
        }

        @Test
        @DisplayName("이슈가 비활성화 상태라 확인에 실패한다.")
        void _inactiveIssue() {
            // given
            ArticleIssueCheckRequestDto requestDto = new ArticleIssueCheckRequestDto(true);
            articleIssue.setEnabled(false);
            given(articleIssueRepository.findById(articleIssue.getId())).willReturn(
                Optional.of(articleIssue));

            // when&then
            assertThrows(InactiveIssueException.class,
                () -> articleIssueCheckService.checkIssue(requestDto, articleIssue.getId(),
                    member));
        }
    }
}

package com.final_10aeat.domain.issue.unit;

import com.final_10aeat.common.enumclass.MemberRole;
import com.final_10aeat.common.exception.ArticleNotFoundException;
import com.final_10aeat.domain.articleIssue.dto.request.ArticleIssueCheckRequestDto;
import com.final_10aeat.domain.articleIssue.dto.response.ArticleIssueCheckResponseDto;
import com.final_10aeat.domain.articleIssue.entity.ArticleIssue;
import com.final_10aeat.domain.articleIssue.entity.ArticleIssueCheck;
import com.final_10aeat.domain.articleIssue.exception.IssueNotFoundException;
import com.final_10aeat.domain.articleIssue.repository.ArticleIssueCheckRepository;
import com.final_10aeat.domain.articleIssue.repository.ArticleIssueRepository;
import com.final_10aeat.domain.articleIssue.service.ArticleIssueCheckService;
import com.final_10aeat.domain.manageArticle.entity.ManageArticle;
import com.final_10aeat.domain.manageArticle.repository.ManageArticleRepository;
import com.final_10aeat.domain.member.entity.Member;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

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
            given(manageArticleRepository.findById(manageArticle.getId())).willReturn(Optional.of(manageArticle));
            given(articleIssueRepository.findFirstByManageArticleOrderByCreatedAtDesc(manageArticle))
                    .willReturn(Optional.of(articleIssue));

            // when
            ArticleIssueCheckResponseDto responseDto = articleIssueCheckService.manageIssueCheck(requestDto,
                    manageArticle.getId(), member);

            // then
            verify(manageArticleRepository).findById(manageArticle.getId());
            verify(articleIssueRepository).findFirstByManageArticleOrderByCreatedAtDesc(manageArticle);
            verify(articleIssueCheckRepository).save(any(ArticleIssueCheck.class));
            Assertions.assertThat(responseDto.title()).isEqualTo(articleIssue.getTitle());
        }

        @Test
        @DisplayName("게시글이 없어 확인에 실패한다.")
        void _articleNotFound() {
            // given
            ArticleIssueCheckRequestDto requestDto = new ArticleIssueCheckRequestDto(true);
            given(manageArticleRepository.findById(manageArticle.getId())).willReturn(Optional.empty());

            // when&then
            assertThrows(ArticleNotFoundException.class,
                    () -> articleIssueCheckService.manageIssueCheck(requestDto, manageArticle.getId(), member));
        }

        @Test
        @DisplayName("이슈가 없어 확인에 실패한다.")
        void _issueNotFound() {
            // given
            ArticleIssueCheckRequestDto requestDto = new ArticleIssueCheckRequestDto(true);
            given(manageArticleRepository.findById(manageArticle.getId())).willReturn(Optional.of(manageArticle));
            given(articleIssueRepository.findFirstByManageArticleOrderByCreatedAtDesc(manageArticle)).willReturn(Optional.empty());

            // when&then
            assertThrows(IssueNotFoundException.class,
                    () -> articleIssueCheckService.manageIssueCheck(requestDto, manageArticle.getId(), member));
        }
    }


}

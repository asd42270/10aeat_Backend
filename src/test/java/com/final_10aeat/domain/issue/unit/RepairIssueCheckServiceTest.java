package com.final_10aeat.domain.issue.unit;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import com.final_10aeat.common.enumclass.MemberRole;
import com.final_10aeat.domain.articleIssue.dto.request.CheckIssueRequestDto;
import com.final_10aeat.domain.articleIssue.entity.ArticleIssue;
import com.final_10aeat.domain.articleIssue.entity.ArticleIssueCheck;
import com.final_10aeat.domain.articleIssue.exception.DisabledIssueException;
import com.final_10aeat.domain.articleIssue.exception.IssueNotFoundException;
import com.final_10aeat.domain.articleIssue.repository.ArticleIssueCheckRepository;
import com.final_10aeat.domain.articleIssue.repository.ArticleIssueRepository;
import com.final_10aeat.domain.articleIssue.service.IssueCheckService;
import com.final_10aeat.domain.member.entity.Member;
import com.final_10aeat.domain.repairArticle.entity.RepairArticle;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class RepairIssueCheckServiceTest {

    @Mock
    private ArticleIssueCheckRepository articleIssueCheckRepository;
    @Mock
    private ArticleIssueRepository articleIssueRepository;

    @InjectMocks
    private IssueCheckService issueCheckService;

    private final Member member = Member.builder()
        .id(1L)
        .email("test@test.com")
        .password("encodedPassword")
        .name("spring")
        .role(MemberRole.OWNER)
        .build();

    private final RepairArticle repairArticle = RepairArticle.builder()
        .id(1L)
        .title("유지관리 게시글")
        .content("유지관리 게시글 내용")
        .build();

    private final ArticleIssue articleIssue = ArticleIssue.builder()
        .id(1L)
        .title("이슈가 발행됐어요")
        .content("이슈에요")
        .repairArticle(repairArticle)
        .enabled(true)
        .build();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Nested
    @DisplayName("checkIssue()는")
    class Context_checkIssue {

        @Test
        @DisplayName("이슈 확인에 성공한다.")
        void _willSuccess() {
            // given
            CheckIssueRequestDto requestDto = new CheckIssueRequestDto(true);
            given(articleIssueRepository.findById(articleIssue.getId())).willReturn(
                Optional.of(articleIssue));

            // when
            issueCheckService.checkIssue(requestDto, articleIssue.getId(), member);

            // then
            verify(articleIssueRepository).findById(articleIssue.getId());
            verify(articleIssueCheckRepository).save(any(ArticleIssueCheck.class));
        }

        @Test
        @DisplayName("게시글이 없어 확인에 실패한다.")
        void _articleNotFound() {
            // given
            CheckIssueRequestDto requestDto = new CheckIssueRequestDto(true);
            given(articleIssueRepository.findById(articleIssue.getId())).willReturn(
                Optional.empty());

            // when&then
            assertThrows(IssueNotFoundException.class,
                () -> issueCheckService.checkIssue(requestDto, articleIssue.getId(),
                    member));
        }

        @Test
        @DisplayName("이슈가 비활성화 상태라 확인에 실패한다.")
        void _inactiveIssue() {
            // given
            CheckIssueRequestDto requestDto = new CheckIssueRequestDto(true);
            articleIssue.setEnabled(false);
            given(articleIssueRepository.findById(articleIssue.getId())).willReturn(
                Optional.of(articleIssue));

            // when&then
            assertThrows(DisabledIssueException.class,
                () -> issueCheckService.checkIssue(requestDto, articleIssue.getId(),
                    member));
        }
    }
}

package com.final_10aeat.domain.issue.unit;

import com.final_10aeat.common.enumclass.MemberRole;
import com.final_10aeat.domain.articleIssue.dto.request.ArticleIssueCheckRequestDto;
import com.final_10aeat.domain.articleIssue.entity.ArticleIssue;
import com.final_10aeat.domain.articleIssue.entity.ArticleIssueCheck;
import com.final_10aeat.domain.articleIssue.exception.IssueNotFoundException;
import com.final_10aeat.domain.articleIssue.repository.ArticleIssueCheckRepository;
import com.final_10aeat.domain.articleIssue.repository.ArticleIssueRepository;
import com.final_10aeat.domain.articleIssue.service.ArticleIssueCheckService;
import com.final_10aeat.domain.member.entity.Member;
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

public class IssueCheckServiceTest {

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

    private final ArticleIssue articleIssue = ArticleIssue.builder()
            .id(1L)
            .title("이슈가 발행됐어요")
            .content("이슈에요")
            .build();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Nested
    @DisplayName("check()는")
    class Context_Check {

        @Test
        @DisplayName("이슈 확인에 성공한다.")
        void _willSuccess() {
            // given
            ArticleIssueCheckRequestDto requestDto = new ArticleIssueCheckRequestDto(true);
            given(articleIssueRepository.findById(articleIssue.getId())).willReturn(Optional.of(articleIssue));

            // when
            articleIssueCheckService.issueCheck(requestDto, 1L, member);

            // then
            verify(articleIssueRepository).findById(articleIssue.getId());
            verify(articleIssueCheckRepository).save(any(ArticleIssueCheck.class));
        }

        @Test
        @DisplayName("이슈가 없어 확인에 실패한다.")
        void _issueNotFound() {
            // given
            ArticleIssueCheckRequestDto requestDto = new ArticleIssueCheckRequestDto(true);
            given(articleIssueRepository.findById(articleIssue.getId())).willReturn(Optional.empty());


            // when&then
            assertThrows(IssueNotFoundException.class,
                    () -> articleIssueCheckService.issueCheck(requestDto, 1L, member));
        }
    }


}

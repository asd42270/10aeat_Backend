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
import com.final_10aeat.domain.member.entity.Member;
import com.final_10aeat.domain.repairArticle.entity.RepairArticle;
import com.final_10aeat.domain.repairArticle.repository.RepairArticleRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class RepairIssueCheckServiceTest {

    @Mock
    private RepairArticleRepository repairArticleRepository;
    @Mock
    private ArticleIssueCheckRepository articleIssueCheckRepository;
    @Mock
    private ArticleIssueRepository articleIssueRepository;
    @Mock
    private RedisTemplate<String, Object> redisTemplate;
    @InjectMocks
    private ArticleIssueCheckService articleIssueCheckService;

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
            .build();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Nested
    @DisplayName("repairIssueCheck()는")
    class Context_repairIssueCheck {

        @Test
        @DisplayName("이슈 확인에 성공한다.")
        void _willSuccess() {
            // given
            ArticleIssueCheckRequestDto requestDto = new ArticleIssueCheckRequestDto(true);
            given(repairArticleRepository.findById(repairArticle.getId())).willReturn(Optional.of(repairArticle));
            given(articleIssueRepository.findFirstByRepairArticleAndDeletedAtIsNullOrderByCreatedAtDesc(repairArticle))
                    .willReturn(Optional.of(articleIssue));
            given(redisTemplate.opsForValue()).willReturn(mock(ValueOperations.class));

            // when
            ArticleIssueCheckResponseDto responseDto = articleIssueCheckService.repairIssueCheck(requestDto,
                    repairArticle.getId(), member);

            // then
            verify(repairArticleRepository).findById(repairArticle.getId());
            verify(articleIssueRepository).findFirstByRepairArticleAndDeletedAtIsNullOrderByCreatedAtDesc(repairArticle);
            verify(articleIssueCheckRepository).save(any(ArticleIssueCheck.class));
            Assertions.assertThat(responseDto.title()).isEqualTo(articleIssue.getTitle());
        }

        @Test
        @DisplayName("게시글이 없어 확인에 실패한다.")
        void _articleNotFound() {
            // given
            ArticleIssueCheckRequestDto requestDto = new ArticleIssueCheckRequestDto(true);
            given(repairArticleRepository.findById(repairArticle.getId())).willReturn(Optional.empty());

            // when&then
            assertThrows(ArticleNotFoundException.class,
                    () -> articleIssueCheckService.repairIssueCheck(requestDto, repairArticle.getId(), member));
        }

        @Test
        @DisplayName("이슈가 없어 확인에 실패한다.")
        void _issueNotFound() {
            // given
            ArticleIssueCheckRequestDto requestDto = new ArticleIssueCheckRequestDto(true);
            given(repairArticleRepository.findById(repairArticle.getId())).willReturn(Optional.of(repairArticle));
            given(articleIssueRepository.findFirstByRepairArticleAndDeletedAtIsNullOrderByCreatedAtDesc(repairArticle)).willReturn(Optional.empty());

            // when&then
            assertThrows(IssueNotFoundException.class,
                    () -> articleIssueCheckService.repairIssueCheck(requestDto, repairArticle.getId(), member));
        }
    }
}

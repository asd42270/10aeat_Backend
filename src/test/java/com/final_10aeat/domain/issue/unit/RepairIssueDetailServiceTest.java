package com.final_10aeat.domain.issue.unit;

import com.final_10aeat.domain.articleIssue.dto.response.ArticleIssueCheckResponseDto;
import com.final_10aeat.domain.articleIssue.entity.ArticleIssue;
import com.final_10aeat.domain.articleIssue.entity.ArticleIssueCheck;
import com.final_10aeat.domain.articleIssue.exception.IssueNotFoundException;
import com.final_10aeat.domain.articleIssue.service.ArticleIssueCheckService;
import com.final_10aeat.domain.repairArticle.entity.RepairArticle;
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

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

public class RepairIssueDetailServiceTest {

    @Mock
    private RedisTemplate<String, Object> redisTemplate;

    @InjectMocks
    private ArticleIssueCheckService articleIssueCheckService;

    private final RepairArticle repairArticle = RepairArticle.builder()
            .id(1L)
            .title("게시글")
            .content("게시글 내용")
            .build();

    private final ArticleIssue articleIssue = ArticleIssue.builder()
            .title("이슈")
            .content("내용")
            .repairArticle(repairArticle)
            .build();

    private final ArticleIssueCheck articleIssueCheck = ArticleIssueCheck.builder()
            .articleIssue(articleIssue)
            .checked(true)
            .build();

    private final ArticleIssueCheckResponseDto responseDto = ArticleIssueCheckResponseDto.builder()
            .id(articleIssueCheck.getId())
            .title(articleIssue.getTitle())
            .content(articleIssue.getContent())
            .check(articleIssueCheck.isChecked())
            .build();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Nested
    @DisplayName("getRepairIssueDetail()은")
    class Context_GetRepairIssueDetail {

        @Test
        @DisplayName("이슈 조회에 성공한다.")
        void _willSuccess() {
            // given
            Long repairArticleId = 1L;
            String key= "repair article" + repairArticle.getId();
            given(redisTemplate.opsForValue()).willReturn(mock(ValueOperations.class));
            given(redisTemplate.opsForValue().get(key)).willReturn(responseDto);

            // when&then
            Assertions.assertThat(articleIssueCheckService.getRepairIssueDetail(repairArticleId).title())
                    .isEqualTo(responseDto.title());
        }

        @Test
        @DisplayName("레디스에 이슈 정보가 없어 조회에 실패한다.")
        void _issueNotFound() {
            // given
            Long repairArticleId = 1L;
            String key= "repair article" + repairArticle.getId();
            given(redisTemplate.opsForValue()).willReturn(mock(ValueOperations.class));
            given(redisTemplate.opsForValue().get(key)).willReturn(null);

            // when&then
            assertThrows(IssueNotFoundException.class,
                    () -> articleIssueCheckService.getRepairIssueDetail(repairArticleId));
        }
    }
}

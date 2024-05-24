package com.final_10aeat.domain.issue.unit;

import com.final_10aeat.common.enumclass.MemberRole;
import com.final_10aeat.common.exception.ArticleNotFoundException;
import com.final_10aeat.domain.articleIssue.dto.ArticleIssuePublishRequestDto;
import com.final_10aeat.domain.articleIssue.entity.ArticleIssue;
import com.final_10aeat.domain.articleIssue.repository.ArticleIssueRepository;
import com.final_10aeat.domain.articleIssue.service.ArticleIssueService;
import com.final_10aeat.domain.manager.entity.Manager;
import com.final_10aeat.domain.repairArticle.entity.RepairArticle;
import com.final_10aeat.domain.repairArticle.repository.RepairArticleRepository;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

public class RepairArticleIssuePublishServiceTest {

    @Mock
    private RepairArticleRepository repairArticleRepository;
    @Mock
    private ArticleIssueRepository articleIssueRepository;
    @InjectMocks
    private ArticleIssueService articleIssueService;

    private final Long articleId = 1L;
    private final String issueTitle = "테스트 이슈";
    private final String issueContent = "이슈가 발행";
    private final String articleTitle = "유지관리 게시글";
    private final String articleContent = "게시글 내용";
    private final String email = "test@naver.com";
    private final String password = "spring";

    private final ArticleIssuePublishRequestDto requestDto = new ArticleIssuePublishRequestDto(issueTitle, issueContent);

    private final Manager manager = Manager.builder()
            .email(email)
            .password(password)
            .role(MemberRole.MANAGER)
            .build();

    private final RepairArticle repairArticle = RepairArticle.builder()
            .id(articleId)
            .title(articleTitle)
            .content(articleContent)
            .build();

    private final ArticleIssue articleIssue = ArticleIssue.builder()
            .title(issueTitle)
            .content(issueContent)
            .manager(manager)
            .repairArticle(repairArticle)
            .build();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Nested
    @DisplayName("repairArticleIssuePublic()은")
    class Context_repairIssuePublish {

        @Test
        @DisplayName("유지보수게시글 이슈 발행에 성공한다.")
        void _willSuccess() {
            // given
            given(repairArticleRepository.findById(articleId)).willReturn(Optional.of(repairArticle));
            given(articleIssueRepository.save(any(ArticleIssue.class))).willReturn(articleIssue);

            // when
            articleIssueService.repairIssuePublish(requestDto, articleId, manager);

            // then
            verify(repairArticleRepository).findById(articleId);
            verify(articleIssueRepository).save(any(ArticleIssue.class));
        }

        @Test
        @DisplayName("게시글이 존재하지 않아 발행에 실패한다.")
        void _articleNotFound() {
            // given
            given(repairArticleRepository.findById(articleId)).willReturn(Optional.of(repairArticle));
            given(articleIssueRepository.save(any(ArticleIssue.class))).willReturn(articleIssue);
            Long wrongArticleId = 123L;

            // when&then
            Assertions.assertThrows(ArticleNotFoundException.class,
                    () -> articleIssueService.repairIssuePublish(requestDto, wrongArticleId, manager));
        }
    }
}

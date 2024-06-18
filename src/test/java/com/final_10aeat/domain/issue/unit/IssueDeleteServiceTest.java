package com.final_10aeat.domain.issue.unit;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

import com.final_10aeat.common.dto.UserIdAndRole;
import com.final_10aeat.common.enumclass.MemberRole;
import com.final_10aeat.common.exception.UnauthorizedAccessException;
import com.final_10aeat.domain.articleIssue.entity.ArticleIssue;
import com.final_10aeat.domain.articleIssue.exception.IssueNotFoundException;
import com.final_10aeat.domain.articleIssue.repository.ArticleIssueRepository;
import com.final_10aeat.domain.articleIssue.service.IssueService;
import com.final_10aeat.domain.manager.entity.Manager;
import com.final_10aeat.domain.repairArticle.entity.RepairArticle;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class IssueDeleteServiceTest {

    @Mock
    private ArticleIssueRepository articleIssueRepository;

    @InjectMocks
    private IssueService issueService;

    private final Manager manager = Manager.builder()
        .id(1L)
        .email("email")
        .password("password")
        .role(MemberRole.MANAGER)
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
        .manager(manager)
        .build();

    private final UserIdAndRole userIdAndRole = new UserIdAndRole(manager.getId(), true);

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Nested
    @DisplayName("deleteIssue()는")
    class Context_deleteIssue {

        @Test
        @DisplayName("삭제에 성공한다.")
        void _willSuccess() {
            // given
            Long issueId = 1L;
            given(articleIssueRepository.findById(issueId)).willReturn(
                Optional.ofNullable(articleIssue));

            // when
            issueService.deleteIssue(issueId, userIdAndRole);

            // then
            then(articleIssueRepository).should(times(1)).findById(issueId);
            then(articleIssueRepository).should(times(1))
                .save(articleIssue); // delete 메서드를 사용하여 상태를 변경한 후 save 호출 확인
        }
    }

    @Test
    @DisplayName("이슈가 존재하지 않아 삭제에 실패한다.")
    void _issueNotFound() {
        // given
        Long issueId = 1L;
        given(articleIssueRepository.findById(issueId)).willReturn(Optional.empty());

        // when&then
        Assertions.assertThrows(IssueNotFoundException.class,
            () -> issueService.deleteIssue(issueId, userIdAndRole));
    }

    @Test
    @DisplayName("권한이 없는 접근으로 인해 실패한다.")
    void _unauthorizedException() {
        // given
        Long issueId = 1L;
        UserIdAndRole wrongUserIdAndRole = new UserIdAndRole(2L, true);
        given(articleIssueRepository.findById(issueId)).willReturn(
            Optional.ofNullable(articleIssue));

        // when&then
        Assertions.assertThrows(UnauthorizedAccessException.class,
            () -> issueService.deleteIssue(issueId, wrongUserIdAndRole));
    }

    @Test
    @DisplayName("매니저가 아니라서 실패한다.")
    void _unauthorizedException_() {
        // given
        Long issueId = 1L;
        UserIdAndRole wrongUserIdAndRole = new UserIdAndRole(manager.getId(), false);
        given(articleIssueRepository.findById(issueId)).willReturn(
            Optional.ofNullable(articleIssue));

        // when&then
        Assertions.assertThrows(UnauthorizedAccessException.class,
            () -> issueService.deleteIssue(issueId, wrongUserIdAndRole));
    }


}

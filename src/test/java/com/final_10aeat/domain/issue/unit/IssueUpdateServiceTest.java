package com.final_10aeat.domain.issue.unit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.final_10aeat.common.dto.UserIdAndRole;
import com.final_10aeat.common.enumclass.MemberRole;
import com.final_10aeat.common.exception.UnauthorizedAccessException;
import com.final_10aeat.domain.articleIssue.dto.request.UpdateIssueRequestDto;
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

public class IssueUpdateServiceTest {

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
        .enabled(true)
        .build();

    private final UserIdAndRole userIdAndRole = new UserIdAndRole(manager.getId(), true);

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Nested
    @DisplayName("updateIssue()는")
    class Context_deleteIssue {

        @Test
        @DisplayName("수정에 성공한다.")
        void _willSuccess() {
            // given
            UpdateIssueRequestDto request = UpdateIssueRequestDto.builder()
                .title("수정함")
                .build();

            given(articleIssueRepository.findById(1L)).willReturn(
                Optional.ofNullable(articleIssue));

            // when
            issueService.updateIssue(request, 1L, userIdAndRole);

            // then
            verify(articleIssueRepository, times(1)).findById(1L);
            verify(articleIssueRepository, times(1)).save(articleIssue);

            assertThat(articleIssue.getTitle()).isEqualTo(request.title());
        }

        @Test
        @DisplayName("이슈가 존재하지 않아 실패한다.")
        void _issueNotFound() {
            // given
            UpdateIssueRequestDto request = UpdateIssueRequestDto.builder()
                .title("수정함")
                .build();

            given(articleIssueRepository.findById(1L)).willReturn(Optional.empty());

            // when&then
            assertThrows(IssueNotFoundException.class,
                () -> issueService.updateIssue(request, 1L, userIdAndRole));
        }

        @Test
        @DisplayName("권한이 없는 접근으로 인해 실패한다.")
        void _unauthorizedException() {
            // given
            UpdateIssueRequestDto request = UpdateIssueRequestDto.builder()
                .title("수정함")
                .build();

            UserIdAndRole wrongUserIdAndRole = new UserIdAndRole(2L, true);

            given(articleIssueRepository.findById(1L)).willReturn(
                Optional.ofNullable(articleIssue));

            // when&then
            Assertions.assertThrows(UnauthorizedAccessException.class,
                () -> issueService.updateIssue(request, 1L, wrongUserIdAndRole));
        }

        @Test
        @DisplayName("매니저가 아니라서 실패한다.")
        void _unauthorizedException_() {
            // given
            UpdateIssueRequestDto request = UpdateIssueRequestDto.builder()
                .title("수정함")
                .build();

            UserIdAndRole wrongUserIdAndRole = new UserIdAndRole(manager.getId(), false);

            given(articleIssueRepository.findById(1L)).willReturn(
                Optional.ofNullable(articleIssue));

            // when&then
            Assertions.assertThrows(UnauthorizedAccessException.class,
                () -> issueService.updateIssue(request, 1L, wrongUserIdAndRole));
        }
    }
}

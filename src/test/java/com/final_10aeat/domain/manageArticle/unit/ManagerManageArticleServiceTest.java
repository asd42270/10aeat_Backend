package com.final_10aeat.domain.manageArticle.unit;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.final_10aeat.common.enumclass.ManagePeriod;
import com.final_10aeat.common.exception.UnauthorizedAccessException;
import com.final_10aeat.common.util.EntityUtil;
import com.final_10aeat.domain.manageArticle.dto.request.CreateManageArticleRequestDto;
import com.final_10aeat.domain.manageArticle.dto.request.ScheduleRequestDto;
import com.final_10aeat.domain.manageArticle.dto.request.UpdateManageArticleRequestDto;
import com.final_10aeat.domain.manageArticle.entity.ManageArticle;
import com.final_10aeat.domain.manageArticle.repository.ManageArticleRepository;
import com.final_10aeat.domain.manageArticle.service.ManagerManageArticleService;
import com.final_10aeat.domain.manager.entity.Manager;
import com.final_10aeat.domain.office.entity.Office;
import com.final_10aeat.domain.repairArticle.exception.ArticleAlreadyDeletedException;
import com.final_10aeat.domain.repairArticle.exception.ArticleNotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class ManagerManageArticleServiceTest {

    @Mock
    private ManageArticleRepository manageArticleRepository;

    @InjectMocks
    private ManagerManageArticleService managerManageArticleService;


    private final Office office = EntityUtil.DEFAULT_OFFICE;

    private final Manager manager = Manager.builder()
        .office(office).build();

    private final ManageArticle article = ManageArticle.builder()
        .id(1L)
        .office(office)
        .build();

    private final UpdateManageArticleRequestDto request = new UpdateManageArticleRequestDto(
        ManagePeriod.WEEK,
        1,
        "Updated Title",
        "Updated Legal Basis",
        "Updated Target",
        "Updated Responsibility",
        "Updated Note"
    );

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("create()에 성공한다")
    void create_WillSuccess() {
        // given
        ScheduleRequestDto schedule1 = new ScheduleRequestDto(
            LocalDateTime.of(2024, 6, 1, 0, 0),
            LocalDateTime.of(2024, 6, 14, 0, 0)
        );

        CreateManageArticleRequestDto request = new CreateManageArticleRequestDto(
            ManagePeriod.WEEK,
            1,
            "유지관리 게시글 제목",
            "법적 근거",
            "관리 대상",
            "담당자/수리업체",
            "비고",
            List.of(schedule1)
        );

        // when
        managerManageArticleService.create(request, manager);

        //then
        verify(manageArticleRepository, times(1)).save(any(ManageArticle.class));
    }

    @Nested
    @DisplayName("update()는 ")
    class Context_ManageArticleUpdate {

        @Test
        @DisplayName("성공한다")
        void _WillSuccess() {
            // given
            UpdateManageArticleRequestDto request = new UpdateManageArticleRequestDto(
                ManagePeriod.WEEK,
                1,
                "Updated Title",
                "Updated Legal Basis",
                "Updated Target",
                "Updated Responsibility",
                "Updated Note"
            );

            when(manageArticleRepository.findById(anyLong())).thenReturn(Optional.of(article));

            // when
            managerManageArticleService.update(1L, request, manager);

            // then
            verify(manageArticleRepository, times(1)).findById(1L);

            assertThat(article.getPeriod()).isEqualTo(request.period());
            assertThat(article.getPeriodCount()).isEqualTo(request.periodCount());
            assertThat(article.getTitle()).isEqualTo(request.title());
        }

        @Test
        @DisplayName("삭제된 게시글에 요청을 보내 실패한다.")
        void DeletedArticle_willFall() {
            // given
            article.delete(LocalDateTime.now());

            when(manageArticleRepository.findById(anyLong())).thenReturn(Optional.of(article));

            // when
            assertThrows(ArticleAlreadyDeletedException.class,
                () -> managerManageArticleService.update(1L, request, manager));
        }

        @Test
        @DisplayName("존재하지 않는 게시글에 요청을 보내 실패한다.")
        void ArticleNotFounds_willFall() {
            // given
            when(manageArticleRepository.findById(anyLong())).thenReturn(Optional.empty());

            // when
            assertThrows(ArticleNotFoundException.class,
                () -> managerManageArticleService.update(1L, request, manager));
        }

        @Test
        @DisplayName("권한이 없는 게시글에 요청을 보내 실패한다.")
        void ArticleUnauthorized_willFall() {
            // given
            Office otherOffice = Office.builder().id(2L).build();
            ManageArticle otherOfficeArticle = ManageArticle.builder()
                .id(1L)
                .office(otherOffice)
                .build();

            when(manageArticleRepository.findById(1L)).thenReturn(Optional.of(otherOfficeArticle));

            // when
            assertThrows(UnauthorizedAccessException.class,
                () -> managerManageArticleService.update(1L, request, manager));
        }
    }

    @Nested
    @DisplayName("delete()는 ")
    class Context_ManageArticleDelete {

        @Test
        @DisplayName("성공한다")
        void _WillSuccess() {
            // given
            when(manageArticleRepository.findById(anyLong())).thenReturn(Optional.of(article));

            // when
            managerManageArticleService.delete(1L, manager);

            // then
            verify(manageArticleRepository, times(1)).findById(1L);

            assertThat(article.isDeleted()).isTrue();
        }

        @Test
        @DisplayName("삭제된 게시글에 요청을 보내 실패한다.")
        void DeletedArticle_willFall() {
            // given
            article.delete(LocalDateTime.now());

            when(manageArticleRepository.findById(anyLong())).thenReturn(Optional.of(article));

            // when
            assertThrows(ArticleAlreadyDeletedException.class,
                () -> managerManageArticleService.delete(1L, manager));
        }

        @Test
        @DisplayName("존재하지 않는 게시글에 요청을 보내 실패한다.")
        void ArticleNotFounds_willFall() {
            // given
            when(manageArticleRepository.findById(anyLong())).thenReturn(Optional.empty());

            // when
            assertThrows(ArticleNotFoundException.class,
                () -> managerManageArticleService.delete(1L, manager));
        }

        @Test
        @DisplayName("권한이 없는 게시글에 요청을 보내 실패한다.")
        void ArticleUnauthorized_willFall() {
            // given
            Office otherOffice = Office.builder().id(2L).build();
            ManageArticle otherOfficeArticle = ManageArticle.builder()
                .id(1L)
                .office(otherOffice)
                .build();

            when(manageArticleRepository.findById(1L)).thenReturn(Optional.of(otherOfficeArticle));

            // when
            assertThrows(UnauthorizedAccessException.class,
                () -> managerManageArticleService.delete(1L, manager));
        }
    }
}

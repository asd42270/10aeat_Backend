package com.final_10aeat.domain.manageArticle.unit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.final_10aeat.common.exception.UnauthorizedAccessException;
import com.final_10aeat.common.util.EntityUtil;
import com.final_10aeat.domain.manageArticle.dto.request.CreateScheduleRequestDto;
import com.final_10aeat.domain.manageArticle.dto.util.ScheduleConverter;
import com.final_10aeat.domain.manageArticle.entity.ManageArticle;
import com.final_10aeat.domain.manageArticle.entity.ManageSchedule;
import com.final_10aeat.domain.manageArticle.exception.ScheduleSizeException;
import com.final_10aeat.domain.manageArticle.exception.ScheduleNotFoundException;
import com.final_10aeat.domain.manageArticle.repository.ManageArticleRepository;
import com.final_10aeat.domain.manageArticle.repository.ManageScheduleRepository;
import com.final_10aeat.domain.manageArticle.service.ManagerScheduleService;
import com.final_10aeat.domain.manager.entity.Manager;
import com.final_10aeat.domain.office.entity.Office;
import com.final_10aeat.common.exception.ArticleAlreadyDeletedException;
import com.final_10aeat.domain.repairArticle.exception.ArticleNotFoundException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class ManagerScheduleServiceTest {

    @Mock
    private ManageArticleRepository manageArticleRepository;
    @Mock
    private ManageScheduleRepository manageScheduleRepository;
    @InjectMocks
    private ManagerScheduleService managerScheduleService;

    private final CreateScheduleRequestDto request = new CreateScheduleRequestDto(
        LocalDateTime.of(2024, 6, 1, 0, 0),
        LocalDateTime.of(2024, 6, 14, 0, 0)
    );
    private final Office office = EntityUtil.DEFAULT_OFFICE;

    private final Manager manager = Manager.builder()
        .office(office).build();

    private final ManageArticle article = ManageArticle.builder()
        .id(1L)
        .office(office)
        .schedules(new ArrayList<>())
        .build();

    private final ManageSchedule manageSchedule = ScheduleConverter.toSchedule(request, article);

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Nested
    @DisplayName("create()는 ")
    class Context_ManageScheduleCreate {

        @Test
        @DisplayName("성공한다")
        void _WillSuccess() {
            // given
            given(manageArticleRepository.findById(anyLong())).willReturn(Optional.of(article));

            // when
            managerScheduleService.register(1L, request, manager);

            // then
            verify(manageArticleRepository, times(1)).findById(1L);

            assertThat(article.getSchedules().size()).isEqualTo(1);
        }

        @Test
        @DisplayName("존재하지 않는 게시글에 요청을 보내 실패한다.")
        void ArticleNotFounds_willFall() {
            // given
            given(manageArticleRepository.findById(anyLong())).willReturn(Optional.empty());

            // when & then
            assertThrows(ArticleNotFoundException.class,
                () -> managerScheduleService.register(1L, request, manager));
        }

        @Test
        @DisplayName("삭제된 게시글에 요청을 보내 실패한다.")
        void DeletedArticle_willFall() {
            // given
            article.delete(LocalDateTime.now());
            given(manageArticleRepository.findById(anyLong())).willReturn(Optional.of(article));

            // when & then
            assertThrows(ArticleAlreadyDeletedException.class,
                () -> managerScheduleService.register(1L, request, manager));
        }

        @Test
        @DisplayName("삭제된 게시글에 요청을 보내 실패한다.")
        void ArticleUnauthorized_willFall() {
            // given
            Office otherOffice = Office.builder().id(2L).build();
            ManageArticle otherOfficeArticle = ManageArticle.builder()
                .id(1L)
                .office(otherOffice)
                .build();

            when(manageArticleRepository.findById(1L)).thenReturn(Optional.of(otherOfficeArticle));

            // when & then
            assertThrows(UnauthorizedAccessException.class,
                () -> managerScheduleService.register(1L, request, manager));
        }
    }

    @Nested
    @DisplayName("complete()는 ")
    class Context_ManageScheduleComplete {

        @Test
        @DisplayName("성공한다")
        void _WillSuccess() {
            // given
            given(manageScheduleRepository.findById(anyLong())).willReturn(
                Optional.of(manageSchedule));

            // when
            managerScheduleService.complete(1L, manager);

            // then
            verify(manageScheduleRepository, times(1)).findById(1L);

            assertTrue(manageSchedule.isComplete());
        }

        @Test
        @DisplayName("존재하지 않는 일정에 요청을 보내 실패한다.")
        void ArticleNotFounds_willFall() {
            // given
            given(manageScheduleRepository.findById(anyLong())).willReturn(Optional.empty());

            // when & then
            assertThrows(ScheduleNotFoundException.class,
                () -> managerScheduleService.complete(1L, manager));
        }

        @Test
        @DisplayName("삭제된 일정에 요청을 보내 실패한다.")
        void DeletedArticle_willFall() {
            // given
            article.delete(LocalDateTime.now());
            ManageSchedule deleteArticleSchedule = ScheduleConverter.toSchedule(request, article);
            given(manageScheduleRepository.findById(anyLong())).willReturn(
                Optional.of(deleteArticleSchedule));

            // when & then
            assertThrows(ArticleAlreadyDeletedException.class,
                () -> managerScheduleService.complete(1L, manager));
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
            ManageSchedule unauthorizedSchedule = ScheduleConverter.toSchedule(request,
                otherOfficeArticle);

            when(manageScheduleRepository.findById(1L)).thenReturn(
                Optional.of(unauthorizedSchedule));

            // when & then
            assertThrows(UnauthorizedAccessException.class,
                () -> managerScheduleService.complete(1L, manager));
        }
    }

    @Nested
    @DisplayName("update()는 ")
    class Context_ManageScheduleUpdate {

        private final CreateScheduleRequestDto updatedRequest = new CreateScheduleRequestDto(
            LocalDateTime.of(2024, 6, 2, 0, 0),
            LocalDateTime.of(2024, 6, 20, 0, 0)
        );

        @Test
        @DisplayName("성공한다")
        void _WillSuccess() {
            // given
            given(manageScheduleRepository.findById(anyLong())).willReturn(
                Optional.of(manageSchedule));

            // when
            managerScheduleService.update(1L, updatedRequest, manager);

            // then
            verify(manageScheduleRepository, times(1)).findById(1L);

            assertThat(manageSchedule.getScheduleStart()).isEqualTo(updatedRequest.scheduleStart());
            assertThat(manageSchedule.getScheduleEnd()).isEqualTo(updatedRequest.scheduleEnd());
        }

        @Test
        @DisplayName("존재하지 않는 일정에 요청을 보내 실패한다.")
        void ArticleNotFounds_willFall() {
            // given
            given(manageScheduleRepository.findById(anyLong())).willReturn(Optional.empty());

            // when & then
            assertThrows(ScheduleNotFoundException.class,
                () -> managerScheduleService.update(1L, updatedRequest, manager));
        }

        @Test
        @DisplayName("삭제된 일정에 요청을 보내 실패한다.")
        void DeletedArticle_willFall() {
            // given
            article.delete(LocalDateTime.now());
            ManageSchedule deleteArticleSchedule = ScheduleConverter.toSchedule(request, article);
            given(manageScheduleRepository.findById(anyLong())).willReturn(
                Optional.of(deleteArticleSchedule));

            // when & then
            assertThrows(ArticleAlreadyDeletedException.class,
                () -> managerScheduleService.update(1L, updatedRequest, manager));
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
            ManageSchedule unauthorizedSchedule = ScheduleConverter.toSchedule(request,
                otherOfficeArticle);

            when(manageScheduleRepository.findById(1L)).thenReturn(
                Optional.of(unauthorizedSchedule));

            // when & then
            assertThrows(UnauthorizedAccessException.class,
                () -> managerScheduleService.update(1L, updatedRequest, manager));
        }
    }

    @Nested
    @DisplayName("delete()는 ")
    class Context_ManageScheduleDelete {

        @BeforeEach
        void setUpAddArticle(){
            article.addSchedule(manageSchedule);
        }

        @Test
        @DisplayName("성공한다")
        void _WillSuccess() {
            // given
            ManageSchedule secondSchedule = ScheduleConverter.toSchedule(request, article);
            article.addSchedule(secondSchedule);
            given(manageScheduleRepository.findById(anyLong())).willReturn(
                Optional.of(manageSchedule));

            // when
            managerScheduleService.delete(1L, manager);

            // then
            verify(manageScheduleRepository, times(1)).findById(1L);
            assertThat(article.getSchedules().size()).isEqualTo(1);
        }

        @Test
        @DisplayName("존재하지 않는 일정에 요청을 보내 실패한다.")
        void ArticleNotFounds_willFall() {
            // given
            given(manageScheduleRepository.findById(anyLong())).willReturn(Optional.empty());

            // when & then
            assertThrows(ScheduleNotFoundException.class,
                () -> managerScheduleService.delete(1L, manager));
        }

        @Test
        @DisplayName("삭제된 일정에 요청을 보내 실패한다.")
        void DeletedArticle_willFall() {
            // given
            article.delete(LocalDateTime.now());
            ManageSchedule deleteArticleSchedule = ScheduleConverter.toSchedule(request, article);
            given(manageScheduleRepository.findById(anyLong())).willReturn(
                Optional.of(deleteArticleSchedule));

            // when & then
            assertThrows(ArticleAlreadyDeletedException.class,
                () -> managerScheduleService.delete(1L, manager));
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
            ManageSchedule unauthorizedSchedule = ScheduleConverter.toSchedule(request,
                otherOfficeArticle);

            when(manageScheduleRepository.findById(1L)).thenReturn(
                Optional.of(unauthorizedSchedule));

            // when & then
            assertThrows(UnauthorizedAccessException.class,
                () -> managerScheduleService.delete(1L, manager));
        }

        @Test
        @DisplayName("성공한다")
        void LastSchedule_willFall() {
            // given
            given(manageScheduleRepository.findById(anyLong())).willReturn(
                Optional.of(manageSchedule));

            // when & then
            assertThrows(ScheduleSizeException.class,
                () -> managerScheduleService.delete(1L, manager));
        }

    }
}

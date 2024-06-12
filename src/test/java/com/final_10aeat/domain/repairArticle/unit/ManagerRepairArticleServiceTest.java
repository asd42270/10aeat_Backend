package com.final_10aeat.domain.repairArticle.unit;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.final_10aeat.common.enumclass.ArticleCategory;
import com.final_10aeat.common.enumclass.Progress;
import com.final_10aeat.common.exception.UnauthorizedAccessException;
import com.final_10aeat.domain.manager.entity.Manager;
import com.final_10aeat.domain.manager.repository.ManagerRepository;
import com.final_10aeat.domain.repairArticle.dto.request.CreateCustomProgressRequestDto;
import com.final_10aeat.domain.repairArticle.dto.request.CreateRepairArticleRequestDto;
import com.final_10aeat.domain.repairArticle.dto.request.UpdateCustomProgressRequestDto;
import com.final_10aeat.domain.repairArticle.dto.request.UpdateRepairArticleRequestDto;
import com.final_10aeat.domain.repairArticle.entity.CustomProgress;
import com.final_10aeat.domain.repairArticle.entity.RepairArticle;
import com.final_10aeat.domain.repairArticle.exception.ArticleAlreadyDeletedException;
import com.final_10aeat.domain.repairArticle.exception.ArticleNotFoundException;
import com.final_10aeat.domain.repairArticle.exception.CustomProgressNotFoundException;
import com.final_10aeat.domain.repairArticle.exception.ManagerNotFoundException;
import com.final_10aeat.domain.repairArticle.repository.CustomProgressRepository;
import com.final_10aeat.domain.repairArticle.repository.RepairArticleRepository;
import com.final_10aeat.domain.repairArticle.service.ManagerRepairArticleService;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

public class ManagerRepairArticleServiceTest {

    @Mock
    private RepairArticleRepository repairArticleRepository;

    @Mock
    private ManagerRepository managerRepository;

    @Mock
    private CustomProgressRepository customProgressRepository;

    @Spy
    @InjectMocks
    private ManagerRepairArticleService managerRepairArticleService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private Manager createTestManager() {
        return Manager.builder()
            .id(1L)
            .email("manager@example.com")
            .password("password")
            .name("김관리")
            .phoneNumber("010-1234-5678")
            .build();
    }

    private RepairArticle createTestRepairArticle(Manager manager) {
        return RepairArticle.builder()
            .id(1L)
            .category(ArticleCategory.REPAIR)
            .progress(Progress.INPROGRESS)
            .title("유지보수 게시글 제목")
            .content("내용")
            .startConstruction(LocalDateTime.now())
            .endConstruction(LocalDateTime.now().plusDays(1))
            .company("수리 업체 이름")
            .companyWebsite("http://testcompany.com")
            .manager(manager)
            .images(new ArrayList<>())
            .customProgressSet(new HashSet<>())
            .build();
    }

    private CustomProgress createTestCustomProgress(RepairArticle repairArticle) {
        return CustomProgress.builder()
            .id(1L)
            .title("안건진행사항 제목")
            .content("상세 내용")
            .startSchedule(LocalDateTime.now())
            .inProgress(false)
            .repairArticle(repairArticle)
            .build();
    }

    @Nested
    @DisplayName("createRepairArticle()는")
    class Context_CreateRepairArticle {

        @Test
        @DisplayName("성공적으로 게시글을 생성한다.")
        void _willSuccess() {
            Manager manager = createTestManager();
            CreateRepairArticleRequestDto requestDto = new CreateRepairArticleRequestDto(
                ArticleCategory.REPAIR,
                Progress.INPROGRESS,
                "유지보수 게시글 제목",
                "내용내용",
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(1),
                "수리 업체 이름",
                "http://testcompany.com",
                List.of()
            );

            when(managerRepository.findById(anyLong())).thenReturn(Optional.of(manager));
            when(repairArticleRepository.save(any(RepairArticle.class))).thenAnswer(invocation -> {
                RepairArticle savedArticle = invocation.getArgument(0);
                savedArticle.setImages(Collections.emptyList());
                return savedArticle;
            });

            assertDoesNotThrow(
                () -> managerRepairArticleService.createRepairArticle(requestDto, 1L));
            verify(managerRepository).findById(anyLong());
            verify(repairArticleRepository).save(any(RepairArticle.class));
        }

        @Test
        @DisplayName("관리자를 찾을 수 없는 경우 예외를 발생시킨다.")
        void NotFound_willFail() {
            CreateRepairArticleRequestDto requestDto = new CreateRepairArticleRequestDto(
                ArticleCategory.REPAIR,
                Progress.INPROGRESS,
                "유지보수 게시글 제목",
                "내용내용",
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(1),
                "수리 업체 이름",
                "http://testcompany.com",
                Collections.singletonList(1L)
            );

            when(managerRepository.findById(anyLong())).thenReturn(Optional.empty());

            assertThrows(ManagerNotFoundException.class,
                () -> managerRepairArticleService.createRepairArticle(requestDto, 1L));
            verify(managerRepository).findById(anyLong());
            verify(repairArticleRepository, never()).save(any(RepairArticle.class));
        }
    }

    @Nested
    @DisplayName("deleteRepairArticleById()는")
    class Context_DeleteRepairArticle {

        @Test
        @DisplayName("성공적으로 게시글을 삭제한다.")
        void _willSuccess() {
            RepairArticle article = createTestRepairArticle(createTestManager());
            when(repairArticleRepository.findById(anyLong())).thenReturn(Optional.of(article));

            assertDoesNotThrow(() -> managerRepairArticleService.deleteRepairArticleById(1L, 1L));
            verify(repairArticleRepository).findById(anyLong());
            assertTrue(article.isDeleted());
        }

        @Test
        @DisplayName("게시글을 찾을 수 없는 경우 예외를 발생시킨다.")
        void NotFound_willFail() {
            when(repairArticleRepository.findById(anyLong())).thenReturn(Optional.empty());

            assertThrows(ArticleNotFoundException.class,
                () -> managerRepairArticleService.deleteRepairArticleById(1L, 1L));
            verify(repairArticleRepository).findById(anyLong());
        }

        @Test
        @DisplayName("권한이 없는 경우 예외를 발생시킨다.")
        void Unauthorized_willFail() {
            RepairArticle article = createTestRepairArticle(createTestManager());
            when(repairArticleRepository.findById(anyLong())).thenReturn(Optional.of(article));

            assertThrows(UnauthorizedAccessException.class, () -> {
                managerRepairArticleService.deleteRepairArticleById(1L, 2L);
            });
        }
    }

    @Nested
    @DisplayName("updateRepairArticle()는")
    class Context_UpdateRepairArticle {

        @Test
        @DisplayName("성공적으로 게시글을 업데이트한다.")
        void _willSuccess() {
            RepairArticle article = createTestRepairArticle(createTestManager());
            UpdateRepairArticleRequestDto requestDto = new UpdateRepairArticleRequestDto(
                ArticleCategory.REPAIR,
                Progress.COMPLETE,
                "제목 수정",
                "내용 수정",
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(2),
                "수리 업체 수정",
                "http://updatedcompany.com"
            );

            when(repairArticleRepository.findById(anyLong())).thenReturn(Optional.of(article));

            assertDoesNotThrow(
                () -> managerRepairArticleService.updateRepairArticle(1L, requestDto, 1L));
            verify(repairArticleRepository).findById(anyLong());
            verify(repairArticleRepository).save(any(RepairArticle.class));
        }

        @Test
        @DisplayName("게시글을 찾을 수 없는 경우 예외 발생")
        void NotFound_willFail() {
            UpdateRepairArticleRequestDto requestDto = new UpdateRepairArticleRequestDto(
                ArticleCategory.REPAIR,
                Progress.COMPLETE,
                "Updated Title",
                "Updated Content",
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(2),
                "Updated Company",
                "http://updatedcompany.com"
            );

            when(repairArticleRepository.findById(anyLong())).thenReturn(Optional.empty());

            assertThrows(ArticleNotFoundException.class,
                () -> managerRepairArticleService.updateRepairArticle(1L, requestDto, 1L));
            verify(repairArticleRepository).findById(anyLong());
        }

        @Test
        @DisplayName("삭제된 게시글은 업데이트할 수 없다")
        void BadRequest_willFail() {
            RepairArticle article = createTestRepairArticle(createTestManager());
            article.delete(LocalDateTime.now());
            when(repairArticleRepository.findById(anyLong())).thenReturn(Optional.of(article));

            UpdateRepairArticleRequestDto requestDto = new UpdateRepairArticleRequestDto(
                ArticleCategory.REPAIR,
                Progress.COMPLETE,
                "제목 수정",
                "내용 수정",
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(2),
                "수리 업체 수정",
                "http://updatedcompany.com"
            );

            assertThrows(ArticleAlreadyDeletedException.class,
                () -> managerRepairArticleService.updateRepairArticle(1L, requestDto, 1L));
        }
    }

    @Nested
    @DisplayName("CustomProgress 생성")
    class Context_CreateCustomProgress {

        @Test
        @DisplayName("성공적으로 CustomProgress 생성")
        void willSuccess() {
            RepairArticle article = createTestRepairArticle(createTestManager());
            CreateCustomProgressRequestDto requestDto = new CreateCustomProgressRequestDto(
                LocalDateTime.now(),
                "안건진행사항 제목",
                "상세 내용"
            );

            when(repairArticleRepository.findById(anyLong())).thenReturn(Optional.of(article));
            when(customProgressRepository.save(any(CustomProgress.class))).thenAnswer(
                invocation -> {
                    CustomProgress savedProgress = invocation.getArgument(0);
                    return savedProgress;
                });

            assertDoesNotThrow(
                () -> managerRepairArticleService.createCustomProgress(1L, 1L, requestDto));
            verify(repairArticleRepository).findById(anyLong());
            verify(customProgressRepository).save(any(CustomProgress.class));
        }

        @Test
        @DisplayName("게시글을 찾을 수 없는 경우 예외 발생")
        void NotFound_willFail() {
            CreateCustomProgressRequestDto requestDto = new CreateCustomProgressRequestDto(
                LocalDateTime.now(),
                "Progress Title",
                "Progress Content"
            );

            when(repairArticleRepository.findById(anyLong())).thenReturn(Optional.empty());

            assertThrows(ArticleNotFoundException.class,
                () -> managerRepairArticleService.createCustomProgress(1L, 1L, requestDto));
            verify(repairArticleRepository).findById(anyLong());
            verify(customProgressRepository, never()).save(any(CustomProgress.class));
        }
    }

    @Nested
    @DisplayName("CustomProgress 업데이트")
    class Context_UpdateCustomProgress {

        @Test
        @DisplayName("성공적으로 CustomProgress 업데이트")
        void _willSuccess() {
            RepairArticle article = createTestRepairArticle(createTestManager());
            CustomProgress customProgress = createTestCustomProgress(article);
            UpdateCustomProgressRequestDto requestDto = new UpdateCustomProgressRequestDto(
                LocalDateTime.now(),
                "안건진행사항 제목 수정",
                "내용 수정수정",
                true
            );
            when(customProgressRepository.findById(anyLong())).thenReturn(
                Optional.of(customProgress));

            assertDoesNotThrow(
                () -> managerRepairArticleService.updateCustomProgress(1L, 1L, requestDto));
            verify(customProgressRepository).findById(anyLong());
            verify(customProgressRepository).save(any(CustomProgress.class));
        }

        @Test
        @DisplayName("권한이 없는 경우 예외 발생")
        void Unauthorized_willFail() {
            RepairArticle article = createTestRepairArticle(createTestManager());
            CustomProgress customProgress = createTestCustomProgress(article);
            UpdateCustomProgressRequestDto requestDto = new UpdateCustomProgressRequestDto(
                LocalDateTime.now(),
                "안건진행사항 제목 수정",
                "내용 수정수정",
                true
            );

            when(customProgressRepository.findById(anyLong())).thenReturn(
                Optional.of(customProgress));

            assertThrows(UnauthorizedAccessException.class,
                () -> managerRepairArticleService.updateCustomProgress(1L, 2L, requestDto));
        }

        @Test
        @DisplayName("CustomProgress를 찾을 수 없는 경우 예외 발생")
        void NotFound_willFail() {
            UpdateCustomProgressRequestDto requestDto = new UpdateCustomProgressRequestDto(
                LocalDateTime.now(),
                "안건진행사항 제목 수정",
                "내용 수정수정",
                true
            );

            when(customProgressRepository.findById(anyLong())).thenReturn(Optional.empty());

            assertThrows(CustomProgressNotFoundException.class,
                () -> managerRepairArticleService.updateCustomProgress(1L, 1L, requestDto));
            verify(customProgressRepository).findById(anyLong());
        }
    }
}

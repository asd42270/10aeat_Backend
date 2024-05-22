package com.final_10aeat.domain.repairArticle.scheduler;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.final_10aeat.common.enumclass.ArticleCategory;
import com.final_10aeat.common.enumclass.Progress;
import com.final_10aeat.common.scheduler.DeleteRepairArticleScheduler;
import com.final_10aeat.domain.repairArticle.entity.RepairArticle;
import com.final_10aeat.domain.repairArticle.repository.RepairArticleRepository;
import java.time.LocalDateTime;
import java.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class DeleteRepairArticleSchedulerTest {

    @Mock
    private RepairArticleRepository repairArticleRepository;

    @InjectMocks
    private DeleteRepairArticleScheduler deleteRepairArticleScheduler;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCleanUpOldSoftDeletedArticles() {
        // Given
        LocalDateTime oneYearAgo = LocalDateTime.now().minusYears(1);
        RepairArticle article1 = createTestRepairArticle();
        RepairArticle article2 = createTestRepairArticle();

        when(repairArticleRepository.findSoftDeletedBefore(any(LocalDateTime.class)))
            .thenReturn(Arrays.asList(article1, article2));

        // When
        deleteRepairArticleScheduler.OldSoftDeletedArticles();

        // Then
        ArgumentCaptor<LocalDateTime> captor = ArgumentCaptor.forClass(LocalDateTime.class);
        verify(repairArticleRepository, times(1)).findSoftDeletedBefore(captor.capture());
        assertEquals(oneYearAgo.getYear(), captor.getValue().getYear());

        verify(repairArticleRepository, times(1)).delete(article1);
        verify(repairArticleRepository, times(1)).delete(article2);
    }

    private RepairArticle createTestRepairArticle() {
        return RepairArticle.builder()
            .progress(Progress.INPROGRESS)
            .category(ArticleCategory.REPAIR)
            .title("유지보수 게시글 제목")
            .content("유지보수 게시글 내용")
            .startConstruction(LocalDateTime.now())
            .endConstruction(LocalDateTime.now().plusDays(1))
            .company("김관리 인테리어 업체")
            .companyWebsite("http://test.com")
            .build();
    }
}

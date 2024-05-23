package com.final_10aeat.common.scheduler;

import com.final_10aeat.domain.repairArticle.entity.RepairArticle;
import com.final_10aeat.domain.repairArticle.repository.RepairArticleRepository;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DeleteRepairArticleScheduler {

    private final RepairArticleRepository repairArticleRepository;

    @Scheduled(cron = "0 0 2 * * ?")
    public void OldSoftDeletedArticles() {
        LocalDateTime oneYearAgo = LocalDateTime.now().minusYears(1);
        List<RepairArticle> articlesToDelete = repairArticleRepository.findSoftDeletedBefore(oneYearAgo);

        for (RepairArticle article : articlesToDelete) {
            repairArticleRepository.delete(article);
        }
    }
}

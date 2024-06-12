package com.final_10aeat.common.scheduler;

import com.final_10aeat.domain.repairArticle.repository.RepairArticleImageRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DeleteArticleImageScheduler {

    private final RepairArticleImageRepository repairArticleImageRepository;

    @Scheduled(cron = "0 0 2 * * *")
    public void cleanupOrphanAndNonExistentImages() {
        List<Long> imageIdsToDelete = repairArticleImageRepository.findIdsOfOrphanAndNonExistentRepairArticleImages();
        if (!imageIdsToDelete.isEmpty()) {
            repairArticleImageRepository.deleteAllByIdIn(imageIdsToDelete);
        }
    }
}

package com.final_10aeat.common.scheduler;

import com.final_10aeat.common.service.UploadImageComponent;
import com.final_10aeat.domain.repairArticle.entity.RepairArticleImage;
import com.final_10aeat.domain.repairArticle.repository.RepairArticleImageRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DeleteArticleImageScheduler {

    private final RepairArticleImageRepository repairArticleImageRepository;
    private final UploadImageComponent uploadImageComponent;

    @Scheduled(cron = "0 0 2 * * *")
    public void cleanupOrphanAndNonExistentImages() {
        List<RepairArticleImage> imagesToDelete = repairArticleImageRepository.findOrphanAndNonExistentRepairArticleImages();
        for (RepairArticleImage image : imagesToDelete) {
            uploadImageComponent.deleteFromS3(image.getImageUrl());
            repairArticleImageRepository.delete(image);
        }
    }
}

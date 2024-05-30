package com.final_10aeat.common.scheduler;

import com.final_10aeat.domain.manageArticle.entity.ManageArticle;
import com.final_10aeat.domain.manageArticle.repository.ManageArticleRepository;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DeleteManageArticleScheduler {

    private final ManageArticleRepository manageArticleRepository;

    @Scheduled(cron = "0 0 3 * * ?")
    public void OldSoftDeletedArticles() {
        LocalDateTime oneYearAgo = LocalDateTime.now().minusYears(1);
        List<ManageArticle> deletedArticles = manageArticleRepository.
            findAllByDeletedAtBeforeAndDeletedAtNotNull(oneYearAgo);

        manageArticleRepository.deleteAll(deletedArticles);
    }
}

package com.final_10aeat.common.scheduler;

import com.final_10aeat.common.enumclass.ManagePeriod;
import com.final_10aeat.common.enumclass.Progress;
import com.final_10aeat.domain.manageArticle.entity.ManageArticle;
import com.final_10aeat.domain.manageArticle.repository.ManageArticleRepository;
import com.final_10aeat.domain.repairArticle.entity.RepairArticle;
import com.final_10aeat.domain.repairArticle.repository.RepairArticleRepository;
import jakarta.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

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

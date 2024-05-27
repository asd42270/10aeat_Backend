package com.final_10aeat.domain.repairArticle.service;

import com.final_10aeat.common.enumclass.ArticleCategory;
import com.final_10aeat.common.enumclass.Progress;
import com.final_10aeat.domain.repairArticle.dto.response.RepairArticleResponseDto;
import com.final_10aeat.domain.repairArticle.dto.response.RepairArticleSummaryDto;
import com.final_10aeat.domain.repairArticle.entity.RepairArticle;
import com.final_10aeat.domain.repairArticle.repository.RepairArticleRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OwnerRepairArticleService {

    private final RepairArticleRepository repairArticleRepository;


    public RepairArticleSummaryDto getRepairArticleSummary(Long managerId) {
        long total = repairArticleRepository.countByManagerId(managerId);
        long inProgressAndPending = repairArticleRepository.countByManagerIdAndProgressIn(managerId,
            List.of(Progress.INPROGRESS, Progress.PENDING));
        long completed = repairArticleRepository.countByManagerIdAndProgress(managerId,
            Progress.COMPLETE);

        return new RepairArticleSummaryDto(total, inProgressAndPending, completed);
    }

    public List<RepairArticleResponseDto> getAllRepairArticles(Long officeId,
        List<Progress> progresses,
        ArticleCategory category) {
        List<RepairArticle> articles = repairArticleRepository.findByOfficeIdAndProgressInAndCategory(
            officeId, progresses, category);
        return articles.stream().map(article -> {
            boolean isNewArticle = article.getCreatedAt().plusDays(1).isAfter(LocalDateTime.now());
            return new RepairArticleResponseDto(
                article.getId(),
                article.getCategory().name(),
                article.getManager().getName(),
                article.getProgress().name(),
                article.getTitle(),
                article.getStartConstruction(),
                article.getEndConstruction(),
                10, // 더미 댓글 수
                200, // 더미 조회수
                false, // 더미 저장 여부
                false, // 더미 이슈 확인 여부
                isNewArticle,
                article.getImages().isEmpty() ? null
                    : article.getImages().iterator().next().getImageUrl()
            );
        }).collect(Collectors.toList());
    }
}

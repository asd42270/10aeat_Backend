package com.final_10aeat.domain.repairArticle.service;

import com.final_10aeat.common.dto.UserIdAndRole;
import com.final_10aeat.common.enumclass.Progress;
import com.final_10aeat.common.exception.ArticleNotFoundException;
import com.final_10aeat.domain.articleIssue.repository.ArticleIssueCheckRepository;
import com.final_10aeat.domain.repairArticle.dto.response.CustomProgressResponseDto;
import com.final_10aeat.domain.repairArticle.dto.response.RepairArticleDetailDto;
import com.final_10aeat.domain.repairArticle.dto.response.RepairArticleSummaryDto;
import com.final_10aeat.domain.repairArticle.entity.ArticleView;
import com.final_10aeat.domain.repairArticle.entity.CustomProgress;
import com.final_10aeat.domain.repairArticle.entity.RepairArticle;
import com.final_10aeat.domain.repairArticle.entity.RepairArticleImage;
import com.final_10aeat.domain.repairArticle.repository.ArticleViewRepository;
import com.final_10aeat.domain.repairArticle.repository.RepairArticleRepository;
import com.final_10aeat.domain.save.repository.ArticleSaveRepository;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class GetRepairArticleService {

    private final RepairArticleRepository repairArticleRepository;
    private final ArticleSaveRepository articleSaveRepository;
    private final ArticleIssueCheckRepository articleIssueCheckRepository;
    private final ArticleViewRepository articleViewRepository;

    public RepairArticleSummaryDto getRepairArticleSummary(Long officeId,
        UserIdAndRole userIdAndRole) {
        long total = repairArticleRepository.countByOfficeId(officeId);
        long inProgressAndPending = repairArticleRepository.countByOfficeIdAndProgressIn(officeId,
            List.of(Progress.INPROGRESS, Progress.PENDING));
        long completed = repairArticleRepository.countByOfficeIdAndProgress(officeId,
            Progress.COMPLETE);

        List<RepairArticle> inProgressArticles = repairArticleRepository.findByOfficeIdAndProgressIn(
            officeId,
            List.of(Progress.INPROGRESS, Progress.PENDING));
        List<RepairArticle> completeArticles = repairArticleRepository.findByOfficeIdAndProgressIn(
            officeId,
            List.of(Progress.COMPLETE));

        boolean inProgressRedDot = inProgressArticles.stream()
            .anyMatch(article -> hasRedDotIssue(article, userIdAndRole));

        boolean completeRedDot = completeArticles.stream()
            .anyMatch(article -> hasRedDotIssue(article, userIdAndRole));

        return new RepairArticleSummaryDto(total, inProgressAndPending, inProgressRedDot, completed,
            completeRedDot);
    }

    private boolean hasRedDotIssue(RepairArticle article, UserIdAndRole userIdAndRole) {
        Long userId = userIdAndRole.id();
        Set<Long> checkedIssueIds = articleIssueCheckRepository.findCheckedIssueIdsByMember(userId);
        return article.getActiveIssueId()
            .map(id -> !checkedIssueIds.contains(id))
            .orElse(false);
    }

    public RepairArticleDetailDto getArticleDetails(Long articleId, Long userId,
        boolean isManager) {
        incrementViewCount(articleId, userId, isManager);

        RepairArticle article = repairArticleRepository.findById(articleId)
            .orElseThrow(ArticleNotFoundException::new);

        boolean isSaved = articleSaveRepository.existsByRepairArticleIdAndMemberId(articleId,
            userId);

        List<String> imageUrls = article.getImages().stream()
            .map(RepairArticleImage::getImageUrl)
            .collect(Collectors.toList());

        return new RepairArticleDetailDto(
            article.getCategory().name(),
            article.getProgress().name(),
            isSaved,
            article.getManager().getId(),
            article.getManager().getName(),
            article.getCreatedAt(),
            article.getUpdatedAt(),
            article.getTitle(),
            article.getContent(),
            imageUrls,
            article.getStartConstruction(),
            article.getEndConstruction(),
            article.getCompany(),
            article.getCompanyWebsite()
        );
    }

    @Transactional
    public void incrementViewCount(Long articleId, Long userId, boolean isManager) {
        if (!articleViewRepository.existsByArticleIdAndUserIdAndIsManager(articleId, userId,
            isManager)) {
            ArticleView view = new ArticleView();
            RepairArticle article = repairArticleRepository.findById(articleId)
                .orElseThrow(ArticleNotFoundException::new);
            view.setArticle(article);
            view.setUserId(userId);
            view.setManager(isManager);
            articleViewRepository.save(view);

            article.setViewCount(article.getViewCount() + 1);
            repairArticleRepository.save(article);
        }
    }

    public List<CustomProgressResponseDto> getCustomProgressList(Long articleId) {
        RepairArticle article = repairArticleRepository.findById(articleId)
            .orElseThrow(ArticleNotFoundException::new);

        return article.getCustomProgressSet().stream()
            .map(this::mapToCustomProgressDto)
            .collect(Collectors.toList());
    }

    private CustomProgressResponseDto mapToCustomProgressDto(CustomProgress customProgress) {
        return new CustomProgressResponseDto(
            customProgress.getId(),
            customProgress.getTitle(),
            customProgress.getContent(),
            customProgress.isInProgress(),
            customProgress.getStartSchedule()
        );
    }
}

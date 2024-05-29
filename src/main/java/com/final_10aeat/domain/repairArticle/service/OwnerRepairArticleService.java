package com.final_10aeat.domain.repairArticle.service;

import com.final_10aeat.common.dto.UserIdAndRole;
import com.final_10aeat.common.enumclass.ArticleCategory;
import com.final_10aeat.common.enumclass.Progress;
import com.final_10aeat.common.service.AuthenticationService;
import com.final_10aeat.domain.articleIssue.repository.ArticleIssueCheckRepository;
import com.final_10aeat.domain.comment.repository.CommentRepository;
import com.final_10aeat.domain.repairArticle.dto.response.RepairArticleResponseDto;
import com.final_10aeat.domain.repairArticle.dto.response.RepairArticleSummaryDto;
import com.final_10aeat.domain.repairArticle.entity.RepairArticle;
import com.final_10aeat.domain.repairArticle.repository.RepairArticleRepository;
import com.final_10aeat.domain.save.repository.ArticleSaveRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OwnerRepairArticleService {

    private final RepairArticleRepository repairArticleRepository;
    private final CommentRepository commentRepository;
    private final ArticleSaveRepository articleSaveRepository;
    private final ArticleIssueCheckRepository articleIssueCheckRepository;
    private final AuthenticationService authenticationService;

    public RepairArticleSummaryDto getRepairArticleSummary(Long officeId) {
        long total = repairArticleRepository.countByOfficeId(officeId);
        long inProgressAndPending = repairArticleRepository.countByOfficeIdAndProgressIn(officeId,
            List.of(Progress.INPROGRESS, Progress.PENDING));
        long completed = repairArticleRepository.countByOfficeIdAndProgress(officeId,
            Progress.COMPLETE);

        return new RepairArticleSummaryDto(total, inProgressAndPending, completed);
    }

    public List<RepairArticleResponseDto> getAllRepairArticles(UserIdAndRole userIdAndRole,
        List<Progress> progresses, ArticleCategory category) {
        Long officeId = authenticationService.getUserOfficeId();
        List<RepairArticle> articles = repairArticleRepository.findByOfficeIdAndProgressInAndCategory(
            officeId, progresses, category);

        Set<Long> savedArticleIds;
        Set<Long> checkedIssueIds;
        boolean isManager = userIdAndRole.isManager();

        if (!isManager) {
            List<Long> articleIds = articles.stream().map(RepairArticle::getId)
                .collect(Collectors.toList());
            savedArticleIds = articleSaveRepository.findSavedArticleIdsByMember(userIdAndRole.id(),
                articleIds);
            checkedIssueIds = articleIssueCheckRepository.findCheckedIssueIdsByMember(
                userIdAndRole.id());
        } else {
            checkedIssueIds = new HashSet<>();
            savedArticleIds = new HashSet<>();
        }

        List<RepairArticleResponseDto> isNewArticleList = articles.stream()
            .map(article -> mapToDto(article, savedArticleIds, checkedIssueIds, isManager))
            .filter(RepairArticleResponseDto::isNewArticle)
            .sorted(Comparator.comparing(RepairArticleResponseDto::createdAt).reversed())
            .toList();

        List<RepairArticleResponseDto> isNotNewArticleList = articles.stream()
            .map(article -> mapToDto(article, savedArticleIds, checkedIssueIds, isManager))
            .filter(dto -> !dto.isNewArticle())
            .sorted(Comparator.comparing(RepairArticleResponseDto::redDot)
                .thenComparing(RepairArticleResponseDto::createdAt).reversed())
            .toList();

        List<RepairArticleResponseDto> sortedArticles = new ArrayList<>();
        sortedArticles.addAll(isNewArticleList);
        sortedArticles.addAll(isNotNewArticleList);

        return sortedArticles;
    }

    private RepairArticleResponseDto mapToDto(RepairArticle article, Set<Long> savedArticleIds,
        Set<Long> checkedIssueIds, boolean isManager) {
        boolean isNewArticle = article.getCreatedAt().plusDays(1).isAfter(LocalDateTime.now());
        int commentCount = (int) commentRepository.countByRepairArticleIdAndDeletedAtIsNull(
            article.getId());
        boolean isSaved = savedArticleIds.contains(article.getId());
        boolean hasIssue = !isManager && article.hasIssue() && !checkedIssueIds.contains(
            article.getIssue().getId());

        return new RepairArticleResponseDto(
            article.getId(),
            article.getCategory().name(),
            article.getManager().getName(),
            article.getProgress().name(),
            article.getTitle(),
            article.getStartConstruction(),
            article.getEndConstruction(),
            article.getCreatedAt(),
            commentCount,
            200,
            isSaved,
            hasIssue,
            isNewArticle,
            article.getImages().isEmpty() ? null
                : article.getImages().iterator().next().getImageUrl()
        );
    }
}

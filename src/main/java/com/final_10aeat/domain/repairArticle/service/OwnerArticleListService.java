package com.final_10aeat.domain.repairArticle.service;

import com.final_10aeat.common.enumclass.ArticleCategory;
import com.final_10aeat.common.enumclass.Progress;
import com.final_10aeat.domain.articleIssue.repository.ArticleIssueCheckRepository;
import com.final_10aeat.domain.comment.repository.CommentRepository;
import com.final_10aeat.domain.repairArticle.dto.response.OwnerRepairArticleResponseDto;
import com.final_10aeat.domain.repairArticle.entity.RepairArticle;
import com.final_10aeat.domain.repairArticle.repository.RepairArticleRepository;
import com.final_10aeat.domain.save.repository.ArticleSaveRepository;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OwnerArticleListService {

    private final RepairArticleRepository repairArticleRepository;
    private final CommentRepository commentRepository;
    private final ArticleSaveRepository articleSaveRepository;
    private final ArticleIssueCheckRepository articleIssueCheckRepository;

    public List<OwnerRepairArticleResponseDto> getAllRepairArticles(Long officeId, Long userId,
        List<Progress> progresses, ArticleCategory category) {
        List<RepairArticle> articles = repairArticleRepository.findByOfficeIdAndProgressInAndCategoryOrderByCreatedAtDesc(
            officeId, progresses, category);

        Set<Long> savedArticleIds;
        Set<Long> checkedIssueIds;

        List<Long> articleIds = articles.stream().map(RepairArticle::getId)
            .collect(Collectors.toList());
        savedArticleIds = articleSaveRepository.findSavedArticleIdsByMember(userId, articleIds);
        checkedIssueIds = articleIssueCheckRepository.findCheckedIssueIdsByMember(userId);

        return articles.stream()
            .map(article -> mapToDto(article, savedArticleIds, checkedIssueIds))
            .sorted(Comparator.comparing(OwnerRepairArticleResponseDto::redDot).reversed())
            .collect(Collectors.toList());
    }

    private OwnerRepairArticleResponseDto mapToDto(RepairArticle article, Set<Long> savedArticleIds,
        Set<Long> checkedIssueIds) {
        int commentCount = (int) commentRepository.countByRepairArticleIdAndDeletedAtIsNull(
            article.getId());
        boolean isSaved = savedArticleIds.contains(article.getId());
        boolean hasIssue =
            article.hasIssue() && !checkedIssueIds.contains(article.getIssue().getId());
        int viewCount = article.getViewCount();

        return new OwnerRepairArticleResponseDto(
            article.getId(),
            article.getCategory().name(),
            article.getManager().getName(),
            article.getProgress().name(),
            article.getTitle(),
            article.getStartConstruction(),
            article.getEndConstruction(),
            article.getCreatedAt(),
            article.getUpdatedAt(),
            commentCount,
            viewCount,
            isSaved,
            hasIssue,
            article.getImages().isEmpty() ? null
                : article.getImages().iterator().next().getImageUrl()
        );
    }
}

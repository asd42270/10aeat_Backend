package com.final_10aeat.domain.repairArticle.service;

import com.final_10aeat.common.enumclass.ArticleCategory;
import com.final_10aeat.common.enumclass.Progress;
import com.final_10aeat.domain.repairArticle.dto.response.ManagerRepairArticleResponseDto;
import com.final_10aeat.domain.repairArticle.entity.RepairArticle;
import com.final_10aeat.domain.comment.repository.CommentRepository;
import com.final_10aeat.domain.repairArticle.repository.RepairArticleRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ManagerArticleListService {

    private final RepairArticleRepository repairArticleRepository;
    private final CommentRepository commentRepository;

    public List<ManagerRepairArticleResponseDto> getAllRepairArticles(Long officeId,
        List<Progress> progresses, ArticleCategory category) {
        List<RepairArticle> articles = repairArticleRepository.findByOfficeIdAndProgressInAndCategoryOrderByCreatedAtDesc(
            officeId, progresses, category);

        return articles.stream()
            .sorted((a, b) -> b.getCreatedAt().compareTo(a.getUpdatedAt()))
            .map(this::mapToDto)
            .collect(Collectors.toList());
    }

    private ManagerRepairArticleResponseDto mapToDto(RepairArticle article) {
        int commentCount = (int) commentRepository.countByRepairArticleIdAndDeletedAtIsNull(
            article.getId());
        int viewCount = article.getViewCount();

        return new ManagerRepairArticleResponseDto(
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
            article.getImages().isEmpty() ? null
                : article.getImages().iterator().next().getImageUrl()
        );
    }
}

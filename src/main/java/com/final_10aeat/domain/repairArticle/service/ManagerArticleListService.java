package com.final_10aeat.domain.repairArticle.service;

import com.final_10aeat.common.enumclass.ArticleCategory;
import com.final_10aeat.common.enumclass.Progress;
import com.final_10aeat.domain.comment.repository.CommentRepository;
import com.final_10aeat.domain.repairArticle.dto.response.ManagerRepairArticleResponseDto;
import com.final_10aeat.domain.repairArticle.entity.RepairArticle;
import com.final_10aeat.domain.repairArticle.repository.RepairArticleRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ManagerArticleListService {

    private final RepairArticleRepository repairArticleRepository;
    private final CommentRepository commentRepository;

    public Page<ManagerRepairArticleResponseDto> getAllRepairArticles(Long officeId,
        List<Progress> progresses, ArticleCategory category, Pageable pageable) {
        Page<RepairArticle> articles = repairArticleRepository.findByOfficeIdAndProgressInAndCategoryOrderByUpdatedAtDesc(
            officeId, progresses, category, pageable);

        return articles.map(this::mapToDto);
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

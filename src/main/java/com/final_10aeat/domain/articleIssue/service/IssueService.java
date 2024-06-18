package com.final_10aeat.domain.articleIssue.service;

import static java.util.Optional.ofNullable;

import com.final_10aeat.common.dto.UserIdAndRole;
import com.final_10aeat.common.exception.ArticleNotFoundException;
import com.final_10aeat.common.exception.UnauthorizedAccessException;
import com.final_10aeat.domain.articleIssue.dto.request.CreateIssueRequestDto;
import com.final_10aeat.domain.articleIssue.dto.request.UpdateIssueRequestDto;
import com.final_10aeat.domain.articleIssue.dto.response.IssueHistoryResponseDto;
import com.final_10aeat.domain.articleIssue.entity.ArticleIssue;
import com.final_10aeat.domain.articleIssue.exception.IssueNotFoundException;
import com.final_10aeat.domain.articleIssue.repository.ArticleIssueRepository;
import com.final_10aeat.domain.manageArticle.entity.ManageArticle;
import com.final_10aeat.domain.manageArticle.repository.ManageArticleRepository;
import com.final_10aeat.domain.manager.entity.Manager;
import com.final_10aeat.domain.repairArticle.entity.RepairArticle;
import com.final_10aeat.domain.repairArticle.repository.RepairArticleRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class IssueService {

    private final ArticleIssueRepository articleIssueRepository;
    private final ManageArticleRepository manageArticleRepository;
    private final RepairArticleRepository repairArticleRepository;

    public void manageIssuePublish(CreateIssueRequestDto request, Long id,
        Manager manager) {

        ManageArticle manageArticle = manageArticleRepository.findById(id)
            .orElseThrow(ArticleNotFoundException::new);

        manageArticle.getIssues().forEach(issue -> issue.setEnabled(false));

        ArticleIssue articleIssue = new ArticleIssue(request.title(), request.content(),
            manageArticle, manager);

        manageArticle.getIssues().add(articleIssue);
        articleIssueRepository.save(articleIssue);
    }

    public void repairIssuePublish(CreateIssueRequestDto request, Long id,
        Manager manager) {

        RepairArticle repairArticle = repairArticleRepository.findById(id)
            .orElseThrow(ArticleNotFoundException::new);

        repairArticle.getIssues().forEach(issue -> issue.setEnabled(false));

        ArticleIssue articleIssue = new ArticleIssue(request.title(), request.content(),
            repairArticle, manager);

        repairArticle.getIssues().add(articleIssue);
        articleIssueRepository.save(articleIssue);
    }

    public void updateIssue(UpdateIssueRequestDto request, Long issueId,
        UserIdAndRole userIdAndRole) {
        ArticleIssue articleIssue = articleIssueRepository.findById(issueId)
            .orElseThrow(IssueNotFoundException::new);

        validateManager(userIdAndRole, articleIssue);

        update(request, articleIssue);
    }

    public void deleteIssue(Long issueId, UserIdAndRole userIdAndRole) {
        ArticleIssue articleIssue = articleIssueRepository.findById(issueId)
            .orElseThrow(IssueNotFoundException::new);

        validateManager(userIdAndRole, articleIssue);

        articleIssue.setEnabled(false);
        articleIssue.delete(LocalDateTime.now());
        articleIssueRepository.save(articleIssue);
    }

    private void validateManager(UserIdAndRole userIdAndRole, ArticleIssue articleIssue) {
        if (!userIdAndRole.isManager()) {
            throw new UnauthorizedAccessException();
        }
        if (!articleIssue.getManager().getId().equals(userIdAndRole.id())) {
            throw new UnauthorizedAccessException();
        }
    }

    private void update(UpdateIssueRequestDto request, ArticleIssue articleIssue) {
        ofNullable(request.title()).ifPresent(articleIssue::setTitle);
        ofNullable(request.content()).ifPresent(articleIssue::setContent);
        articleIssueRepository.save(articleIssue);
    }

    public List<IssueHistoryResponseDto> getManageArticleIssueHistory(Long manageArticleId) {
        return articleIssueRepository.findByManageArticleIdAndDeletedAtIsNull(manageArticleId)
            .stream()
            .map(issue -> new IssueHistoryResponseDto(issue.getId(), issue.getTitle(),
                issue.isEnabled(), issue.getCreatedAt()))
            .collect(Collectors.toList());
    }

    public List<IssueHistoryResponseDto> getRepairArticleIssueHistory(Long repairArticleId) {
        return articleIssueRepository.findByRepairArticleIdAndDeletedAtIsNull(repairArticleId)
            .stream()
            .map(issue -> new IssueHistoryResponseDto(issue.getId(), issue.getTitle(),
                issue.isEnabled(), issue.getCreatedAt()))
            .collect(Collectors.toList());
    }
}

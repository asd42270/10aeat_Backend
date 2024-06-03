package com.final_10aeat.domain.articleIssue.service;

import com.final_10aeat.common.dto.UserIdAndRole;
import com.final_10aeat.common.exception.ArticleNotFoundException;
import com.final_10aeat.common.exception.UnauthorizedAccessException;
import com.final_10aeat.domain.articleIssue.dto.request.ArticleIssuePublishRequestDto;
import com.final_10aeat.domain.articleIssue.entity.ArticleIssue;
import com.final_10aeat.domain.articleIssue.exception.IssueNotFoundException;
import com.final_10aeat.domain.articleIssue.repository.ArticleIssueRepository;
import com.final_10aeat.domain.manageArticle.entity.ManageArticle;
import com.final_10aeat.domain.manageArticle.repository.ManageArticleRepository;
import com.final_10aeat.domain.manager.entity.Manager;
import com.final_10aeat.domain.repairArticle.entity.RepairArticle;
import com.final_10aeat.domain.repairArticle.repository.RepairArticleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional
@RequiredArgsConstructor
public class ArticleIssueService {

    private final ArticleIssueRepository articleIssueRepository;
    private final ManageArticleRepository manageArticleRepository;
    private final RepairArticleRepository repairArticleRepository;

    public void manageIssuePublish(ArticleIssuePublishRequestDto request, Long id,
                                   Manager manager) {

        ManageArticle manageArticle = manageArticleRepository.findById(id)
                .orElseThrow(ArticleNotFoundException::new);

        ArticleIssue articleIssue = new ArticleIssue(request.title(), request.content(),
                manageArticle, LocalDateTime.now(), manager);

        articleIssueRepository.save(articleIssue);
    }

    public void repairIssuePublish(ArticleIssuePublishRequestDto request, Long id,
                                   Manager manager) {

        RepairArticle repairArticle = repairArticleRepository.findById(id)
                .orElseThrow(ArticleNotFoundException::new);

        ArticleIssue articleIssue = new ArticleIssue(request.title(), request.content(),
                repairArticle, LocalDateTime.now(), manager);

        articleIssueRepository.save(articleIssue);
    }

    //TODO:OneToOne 관계로 인해 물리삭제로 구현, 이후 연관관계 변경 및 리팩토링 진행 시 논리삭제로 다시 변경 예정
    public void deleteIssue(Long issueId, UserIdAndRole userIdAndRole) {
//        ArticleIssue articleIssue = articleIssueRepository.findByIdAndDeletedAtIsNull(issueId)
//                .orElseThrow(IssueNotFoundException::new);

        ArticleIssue articleIssue = articleIssueRepository.findById(issueId)
                .orElseThrow(IssueNotFoundException::new);

        if (!userIdAndRole.isManager()) {
            throw new UnauthorizedAccessException();
        }

        if (!articleIssue.getManager().getId().equals(userIdAndRole.id())) {
            throw new UnauthorizedAccessException();
        }

//        articleIssue.delete(LocalDateTime.now());

        articleIssueRepository.deleteById(issueId);
    }

}

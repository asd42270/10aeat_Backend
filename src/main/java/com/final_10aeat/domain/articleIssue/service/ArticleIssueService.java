package com.final_10aeat.domain.articleIssue.service;

import com.final_10aeat.common.exception.ArticleNotFoundException;
import com.final_10aeat.domain.articleIssue.dto.request.ArticleIssuePublishRequestDto;
import com.final_10aeat.domain.articleIssue.entity.ArticleIssue;
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

        manageArticle.setIssue(articleIssue);

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
}

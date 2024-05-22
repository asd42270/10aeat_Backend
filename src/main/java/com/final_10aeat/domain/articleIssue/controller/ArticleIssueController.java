package com.final_10aeat.domain.articleIssue.controller;

import com.final_10aeat.domain.articleIssue.dto.ArticleIssuePublishRequestDto;
import com.final_10aeat.domain.articleIssue.service.ArticleIssueService;
import com.final_10aeat.global.util.ResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/managers/articles/")
@PreAuthorize("hasRole('MANAGER')")
@RequiredArgsConstructor
public class ArticleIssueController {

    private final ArticleIssueService articleIssueService;

    @PostMapping("/manage/issue/{manage_article_id}")
    public ResponseDTO<Void> managePublish(
            @PathVariable("manage_article_id") Long id,
            @RequestBody ArticleIssuePublishRequestDto request
    ) {
        articleIssueService.manageIssuePublish(request, id);
        return ResponseDTO.ok();
    }

    @PostMapping("/repair/issue/{repair_article_id}")
    public ResponseDTO<Void> repairPublish(
            @PathVariable("repair_article_id") Long id,
            @RequestBody ArticleIssuePublishRequestDto request
    ) {
        articleIssueService.repairIssuePublish(request, id);
        return ResponseDTO.ok();
    }

}

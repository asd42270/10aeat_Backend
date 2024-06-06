package com.final_10aeat.domain.articleIssue.controller;

import com.final_10aeat.common.dto.UserIdAndRole;
import com.final_10aeat.common.service.AuthenticationService;
import com.final_10aeat.domain.articleIssue.dto.request.ArticleIssuePublishRequestDto;
import com.final_10aeat.domain.articleIssue.dto.request.IssueUpdateRequestDto;
import com.final_10aeat.domain.articleIssue.dto.response.IssueHistoryResponseDto;
import com.final_10aeat.domain.articleIssue.service.ArticleIssueService;
import com.final_10aeat.global.security.principal.ManagerPrincipal;
import com.final_10aeat.global.util.ResponseDTO;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/managers/articles")
@PreAuthorize("hasRole('MANAGER')")
@RequiredArgsConstructor
public class ArticleIssueController {

    private final ArticleIssueService articleIssueService;
    private final AuthenticationService authenticationService;

    @PostMapping("/manage/issue/{manage_article_id}")
    public ResponseDTO<Void> managePublish(
        @PathVariable("manage_article_id") Long id,
        @RequestBody @Valid ArticleIssuePublishRequestDto request
    ) {
        ManagerPrincipal managerPrincipal = (ManagerPrincipal) SecurityContextHolder.getContext()
            .getAuthentication().getPrincipal();

        articleIssueService.manageIssuePublish(request, id, managerPrincipal.getManager());
        return ResponseDTO.ok();
    }

    @PostMapping("/repair/issue/{repair_article_id}")
    public ResponseDTO<Void> repairPublish(
        @PathVariable("repair_article_id") Long id,
        @RequestBody @Valid ArticleIssuePublishRequestDto request
    ) {
        ManagerPrincipal managerPrincipal = (ManagerPrincipal) SecurityContextHolder.getContext()
            .getAuthentication().getPrincipal();

        articleIssueService.repairIssuePublish(request, id, managerPrincipal.getManager());
        return ResponseDTO.ok();
    }

    @PatchMapping("/issue/{issue_id}")
    public ResponseDTO<Void> updateIssue(
        @PathVariable("issue_id") Long id,
        @RequestBody IssueUpdateRequestDto request
    ) {
        UserIdAndRole userIdAndRole = authenticationService.getCurrentUserIdAndRole();
        articleIssueService.updateIssue(request, id, userIdAndRole);

        return ResponseDTO.ok();
    }

    @DeleteMapping("/issue/{issue_id}")
    public ResponseDTO<Void> deleteIssue(@PathVariable("issue_id") Long id) {
        UserIdAndRole userIdAndRole = authenticationService.getCurrentUserIdAndRole();

        articleIssueService.deleteIssue(id, userIdAndRole);
        return ResponseDTO.ok();
    }

    @GetMapping("/manage/issues/{manageArticleId}")
    public ResponseDTO<List<IssueHistoryResponseDto>> getManageArticleIssueHistory(
        @PathVariable("manageArticleId") Long manageArticleId) {
        List<IssueHistoryResponseDto> issues = articleIssueService.getManageArticleIssueHistory(
            manageArticleId);
        return ResponseDTO.okWithData(issues);
    }

    @GetMapping("/repair/issues/{repairArticleId}")
    public ResponseDTO<List<IssueHistoryResponseDto>> getRepairArticleIssueHistory(
        @PathVariable("repairArticleId") Long repairArticleId) {
        List<IssueHistoryResponseDto> issues = articleIssueService.getRepairArticleIssueHistory(
            repairArticleId);
        return ResponseDTO.okWithData(issues);
    }
}

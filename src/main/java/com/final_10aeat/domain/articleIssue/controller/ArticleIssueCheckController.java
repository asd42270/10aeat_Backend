package com.final_10aeat.domain.articleIssue.controller;

import com.final_10aeat.domain.articleIssue.dto.request.ArticleIssueCheckRequestDto;
import com.final_10aeat.domain.articleIssue.dto.response.ArticleIssueCheckResponseDto;
import com.final_10aeat.domain.articleIssue.service.ArticleIssueCheckService;
import com.final_10aeat.global.security.principal.MemberPrincipal;
import com.final_10aeat.global.util.ResponseDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/issues")
@PreAuthorize("hasRole('USER')")
public class ArticleIssueCheckController {

    private final ArticleIssueCheckService articleIssueCheckService;

    @PostMapping("/check/manage/{manage_article_id}")
    public ResponseDTO<ArticleIssueCheckResponseDto> checkManageIssue(
            @PathVariable("manage_article_id") Long manageArticleId,
            @RequestBody @Valid ArticleIssueCheckRequestDto requestDto
    ) {
        MemberPrincipal memberPrincipal = (MemberPrincipal) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        return ResponseDTO.okWithData(
                articleIssueCheckService.manageIssueCheck(requestDto, manageArticleId,
                        memberPrincipal.getMember())
        );
    }

    @PostMapping("/check/repair/{repair_article_id}")
    public ResponseDTO<ArticleIssueCheckResponseDto> checkRepairIssue(
            @PathVariable("repair_article_id") Long repairArticleId,
            @RequestBody @Valid ArticleIssueCheckRequestDto requestDto
    ) {
        MemberPrincipal memberPrincipal = (MemberPrincipal) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();

        return ResponseDTO.okWithData(
                articleIssueCheckService.repairIssueCheck(requestDto, repairArticleId,
                        memberPrincipal.getMember())
        );
    }

    @GetMapping("/repair/{repair_article_id}")
    public ResponseDTO<ArticleIssueCheckResponseDto> getRepairIssueDetail(
            @PathVariable("repair_article_id") Long articleId
    ) {
        return ResponseDTO.okWithData(articleIssueCheckService.getRepairIssueDetail(articleId));
    }

    @GetMapping("/manage/{manage_article_id}")
    public ResponseDTO<ArticleIssueCheckResponseDto> getManageIssueDetail(
            @PathVariable("manage_article_id") Long articleId
    ) {
        return ResponseDTO.okWithData(articleIssueCheckService.getManageIssueDetail(articleId));
    }
}

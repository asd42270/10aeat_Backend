package com.final_10aeat.domain.articleIssue.controller;

import com.final_10aeat.domain.articleIssue.dto.ArticleIssueCheckRequestDto;
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
@PreAuthorize("hasRole('OWNER')")
public class ArticleIssueCheckController {

    private final ArticleIssueCheckService articleIssueCheckService;

    @PostMapping("/check/{issue_id}")
    public ResponseDTO<Void> check(
            @PathVariable("issue_id") Long issueId,
            @RequestBody @Valid ArticleIssueCheckRequestDto requestDto
    ) {
        MemberPrincipal memberPrincipal = (MemberPrincipal) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();

        articleIssueCheckService.issueCheck(requestDto, issueId, memberPrincipal.getMember());
        return ResponseDTO.ok();
    }
}

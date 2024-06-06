package com.final_10aeat.domain.articleIssue.controller;

import com.final_10aeat.domain.articleIssue.dto.request.ArticleIssueCheckRequestDto;
import com.final_10aeat.domain.articleIssue.dto.response.ArticleIssueCheckResponseDto;
import com.final_10aeat.domain.articleIssue.service.ArticleIssueCheckService;
import com.final_10aeat.domain.member.entity.Member;
import com.final_10aeat.global.security.principal.MemberPrincipal;
import com.final_10aeat.global.util.ResponseDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/articles/issue")
@RequiredArgsConstructor
public class ArticleIssueCheckController {

    private final ArticleIssueCheckService articleIssueCheckService;

    @PostMapping("/check/{issueId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseDTO<Void> checkIssue(
        @PathVariable("issueId") Long issueId,
        @RequestBody @Valid ArticleIssueCheckRequestDto request
    ) {
        MemberPrincipal memberPrincipal = (MemberPrincipal) SecurityContextHolder.getContext()
            .getAuthentication().getPrincipal();
        Member member = memberPrincipal.getMember();

        articleIssueCheckService.checkIssue(request, issueId, member);
        return ResponseDTO.ok();
    }

    @GetMapping("/detail/{issueId}")
    public ResponseDTO<ArticleIssueCheckResponseDto> getIssueDetail(
        @PathVariable("issueId") Long issueId
    ) {
        ArticleIssueCheckResponseDto responseDto = articleIssueCheckService.getIssueDetail(issueId);
        return ResponseDTO.okWithData(responseDto);
    }
}

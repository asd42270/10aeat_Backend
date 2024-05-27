package com.final_10aeat.domain.repairArticle.controller;

import com.final_10aeat.common.enumclass.ArticleCategory;
import com.final_10aeat.common.enumclass.Progress;
import com.final_10aeat.domain.repairArticle.dto.response.RepairArticleResponseDto;
import com.final_10aeat.domain.repairArticle.dto.response.RepairArticleSummaryDto;
import com.final_10aeat.common.exception.UnexpectedPrincipalException;
import com.final_10aeat.domain.repairArticle.service.OwnerRepairArticleService;
import com.final_10aeat.global.security.principal.ManagerPrincipal;
import com.final_10aeat.global.security.principal.MemberPrincipal;
import com.final_10aeat.global.util.ResponseDTO;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/repair/articles")
public class OwnerRepairArticleController {

    private final OwnerRepairArticleService ownerRepairArticleService;

    @GetMapping("/summary")
    public ResponseEntity<ResponseDTO<RepairArticleSummaryDto>> getRepairArticleSummary() {
        Long officeId = getCurrentOfficeId();
        RepairArticleSummaryDto summary = ownerRepairArticleService.getRepairArticleSummary(
            officeId);
        return ResponseEntity.ok(ResponseDTO.okWithData(summary));
    }

    @GetMapping("/list")
    public ResponseEntity<ResponseDTO<List<RepairArticleResponseDto>>> getAllRepairArticles(
        @RequestParam(required = false) List<String> progress,
        @RequestParam(required = false) String category) {

        List<Progress> progressList = determineProgressFilter(progress);
        ArticleCategory categoryFilter = determineCategoryFilter(category);

        Long officeId = getCurrentOfficeId();
        List<RepairArticleResponseDto> articles = ownerRepairArticleService.getAllRepairArticles(
            officeId, progressList, categoryFilter);
        return ResponseEntity.ok(ResponseDTO.okWithData(articles));
    }

    private List<Progress> determineProgressFilter(List<String> progress) {
        if (progress == null || progress.isEmpty()) {
            return Arrays.asList(Progress.values());
        }
        return progress.stream()
            .map(String::toUpperCase)
            .map(Progress::valueOf)
            .collect(Collectors.toList());
    }

    private ArticleCategory determineCategoryFilter(String category) {
        if (category == null || category.isEmpty()) {
            return null;
        }
        return ArticleCategory.valueOf(category.toUpperCase());
    }

    private Long getCurrentOfficeId() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof MemberPrincipal) {
            return ((MemberPrincipal) principal).getMember().getDefaultOffice();
        } else if (principal instanceof ManagerPrincipal) {
            return ((ManagerPrincipal) principal).getManager().getOffice().getId();
        }
        throw new UnexpectedPrincipalException();
    }
}

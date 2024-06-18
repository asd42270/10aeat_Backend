package com.final_10aeat.domain.repairArticle.controller;

import static com.final_10aeat.global.util.PageConverter.convertToCustomPageDto;

import com.final_10aeat.common.dto.PageDto;
import com.final_10aeat.common.enumclass.ArticleCategory;
import com.final_10aeat.common.enumclass.Progress;
import com.final_10aeat.domain.repairArticle.dto.response.CustomProgressResponseDto;
import com.final_10aeat.domain.repairArticle.dto.response.RepairArticleDetailDto;
import com.final_10aeat.domain.repairArticle.dto.response.RepairArticleSummaryDto;
import com.final_10aeat.domain.repairArticle.service.ReadRepairArticleFacade;
import com.final_10aeat.global.util.ResponseDTO;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/repair/articles")
public class ReadRepairArticleController {

    private final ReadRepairArticleFacade readRepairArticleFacade;

    @GetMapping("/summary")
    public ResponseEntity<ResponseDTO<RepairArticleSummaryDto>> getRepairArticleSummary() {
        RepairArticleSummaryDto summary = readRepairArticleFacade.getRepairArticleSummary();
        return ResponseEntity.ok(ResponseDTO.okWithData(summary));
    }

    @GetMapping("/list")
    public ResponseEntity<ResponseDTO<PageDto<?>>> getAllRepairArticles(
        @RequestParam(required = false) List<String> progress,
        @RequestParam(required = false) ArticleCategory category,
        Pageable pageable) {

        List<Progress> progressList = determineProgressFilter(progress);
        Page<?> articles = readRepairArticleFacade.getAllRepairArticles(progressList, category,
            pageable);
        PageDto<?> pageDto = convertToCustomPageDto(articles);
        return ResponseEntity.ok(ResponseDTO.okWithData(pageDto));
    }

    @GetMapping("/{repairArticleId}")
    public ResponseEntity<ResponseDTO<RepairArticleDetailDto>> getRepairArticleDetail(
        @PathVariable Long repairArticleId) {
        RepairArticleDetailDto articleDetails = readRepairArticleFacade.getArticleDetails(
            repairArticleId);

        return ResponseEntity.ok(ResponseDTO.okWithData(articleDetails));
    }

    @GetMapping("/progress/{repairArticleId}")
    public ResponseEntity<ResponseDTO<List<CustomProgressResponseDto>>> getCustomProgressList(
        @PathVariable Long repairArticleId) {
        List<CustomProgressResponseDto> customProgressList = readRepairArticleFacade.getCustomProgressList(
            repairArticleId);
        return ResponseEntity.ok(ResponseDTO.okWithData(customProgressList));
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
}

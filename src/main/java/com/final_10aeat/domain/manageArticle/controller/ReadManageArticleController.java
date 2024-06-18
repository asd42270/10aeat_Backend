package com.final_10aeat.domain.manageArticle.controller;


import static java.util.Optional.ofNullable;

import com.final_10aeat.common.dto.PageDto;
import com.final_10aeat.global.util.PageConverter;
import com.final_10aeat.common.service.AuthUserService;
import com.final_10aeat.domain.manageArticle.dto.response.ManageArticleDetailResponseDto;
import com.final_10aeat.domain.manageArticle.dto.response.ManageArticleListResponseDto;
import com.final_10aeat.domain.manageArticle.dto.response.ManageArticleSummaryResponseDto;
import com.final_10aeat.domain.manageArticle.dto.response.ManageArticleSummaryListResponseDto;
import com.final_10aeat.domain.manageArticle.service.ReadManageArticleService;
import com.final_10aeat.global.util.ResponseDTO;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/manage/articles")
public class ReadManageArticleController {

    private final ReadManageArticleService readManageArticleService;
    private final AuthUserService authUserService;

    @GetMapping("/summary")
    public ResponseDTO<ManageArticleSummaryListResponseDto> summary(
        @RequestParam(required = false) Integer year
    ) {
        Long userOfficeId = authUserService.getUserOfficeId();

        return ResponseDTO.okWithData(
            readManageArticleService.summary(
                userOfficeId,
                ofNullable(year).orElse(LocalDateTime.now().getYear())
            )
        );
    }

    @GetMapping("/{manageArticleId}")
    public ResponseDTO<ManageArticleDetailResponseDto> detailArticle(
        @PathVariable Long manageArticleId) {
        Long userOfficeId = authUserService.getUserOfficeId();
        return ResponseDTO.okWithData(
            readManageArticleService.detailArticle(userOfficeId, manageArticleId)
        );
    }

    @GetMapping("/list")
    public ResponseDTO<PageDto<ManageArticleListResponseDto>> listArticle(
        @RequestParam(required = false) Integer year,
        @RequestParam(required = false) Boolean complete,
        @PageableDefault(size = 20, sort = "id", direction = Direction.DESC) Pageable pageRequest
    ) {
        Long userOfficeId = authUserService.getUserOfficeId();

        if (ofNullable(complete).isEmpty()) {
            return ResponseDTO.okWithData(
                PageConverter.convertToCustomPageDto(
                    readManageArticleService.listArticleByProgress(
                        ofNullable(year).orElseGet(() -> LocalDate.now().getYear()),
                        userOfficeId,
                        pageRequest
                    )
                )
            );
        }

        return ResponseDTO.okWithData(
            PageConverter.convertToCustomPageDto(
                readManageArticleService.listArticleByProgress(
                    ofNullable(year).orElseGet(() -> LocalDate.now().getYear()),
                    userOfficeId,
                    pageRequest,
                    complete
                )
            )
        );
    }

    @GetMapping("/monthly/summary")
    public ResponseDTO<List<ManageArticleSummaryResponseDto>> monthlySummary(
        @RequestParam(required = false) Integer year
    ) {
        Long userOfficeId = authUserService.getUserOfficeId();

        return ResponseDTO.okWithData(
            readManageArticleService.monthlySummary(
                ofNullable(year).orElseGet(() -> LocalDate.now().getYear()),
                userOfficeId
            )
        );
    }

    @GetMapping("/monthly/list")
    public ResponseDTO<PageDto<ManageArticleListResponseDto>> monthlyListArticle(
        @RequestParam(required = false) Integer year,
        @RequestParam(required = false) Integer month,
        @PageableDefault(size = 20, sort = "id", direction = Direction.DESC) Pageable pageRequest
    ) {
        Long userOfficeId = authUserService.getUserOfficeId();
        return ResponseDTO.okWithData(
            PageConverter.convertToCustomPageDto(
                readManageArticleService.monthlyListArticle(
                    userOfficeId,
                    ofNullable(year).orElseGet(() -> LocalDate.now().getYear()),
                    month,
                    pageRequest
                )
            )
        );
    }
}

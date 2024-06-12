package com.final_10aeat.domain.manageArticle.controller;


import static java.util.Optional.*;
import static java.util.Optional.ofNullable;

import com.final_10aeat.common.dto.CustomPageDto;
import com.final_10aeat.common.dto.util.PageConverter;
import com.final_10aeat.common.service.AuthenticationService;
import com.final_10aeat.domain.manageArticle.dto.response.DetailManageArticleResponse;
import com.final_10aeat.domain.manageArticle.dto.response.ListManageArticleResponse;
import com.final_10aeat.domain.manageArticle.dto.response.ManageArticleSummaryResponse;
import com.final_10aeat.domain.manageArticle.dto.response.SummaryManageArticleResponse;
import com.final_10aeat.domain.manageArticle.service.ReadManageArticleService;
import com.final_10aeat.global.util.ResponseDTO;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@PreAuthorize("hasRole('USER')")
@RequestMapping("/manage/articles")
public class ReadManageArticleController {

    private final ReadManageArticleService readManageArticleService;
    private final AuthenticationService authenticationService;

    @GetMapping("/summary")
    public ResponseDTO<SummaryManageArticleResponse> summary(
        @RequestParam(required = false) Integer year
    ) {
        Long userOfficeId = authenticationService.getUserOfficeId();

        return ResponseDTO.okWithData(
            readManageArticleService.summary(
                userOfficeId,
                ofNullable(year).orElse(LocalDateTime.now().getYear())
            )
        );
    }

    @GetMapping("/{manageArticleId}")
    public ResponseDTO<DetailManageArticleResponse> detailArticle(
        @PathVariable Long manageArticleId) {
        Long userOfficeId = authenticationService.getUserOfficeId();
        return ResponseDTO.okWithData(
            readManageArticleService.detailArticle(userOfficeId, manageArticleId)
        );
    }

    @GetMapping("/list")
    public ResponseDTO<CustomPageDto<ListManageArticleResponse>> listArticle(
        @RequestParam(required = false) Integer year,
        @RequestParam(required = false) Boolean complete,
        @PageableDefault(size = 20, sort = "id", direction = Direction.DESC) Pageable pageRequest
    ) {
        Long userOfficeId = authenticationService.getUserOfficeId();

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
    public ResponseDTO<List<ManageArticleSummaryResponse>> monthlySummary(
        @RequestParam(required = false) Integer year
    ) {
        Long userOfficeId = authenticationService.getUserOfficeId();

        return ResponseDTO.okWithData(
            readManageArticleService.monthlySummary(
                ofNullable(year).orElseGet(() -> LocalDate.now().getYear()),
                userOfficeId
            )
        );
    }

    @GetMapping("/monthly/list")
    public ResponseDTO<CustomPageDto<ListManageArticleResponse>> monthlyListArticle(
        @RequestParam(required = false) Integer year,
        @RequestParam(required = false) Integer month,
        @PageableDefault(size = 20, sort = "id", direction = Direction.DESC) Pageable pageRequest
    ) {
        Long userOfficeId = authenticationService.getUserOfficeId();
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

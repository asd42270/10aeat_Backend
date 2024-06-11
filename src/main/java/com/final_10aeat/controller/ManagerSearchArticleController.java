package com.final_10aeat.controller;

import static java.util.Optional.ofNullable;

import com.final_10aeat.common.dto.CustomPageDto;
import com.final_10aeat.common.dto.util.PageConverter;
import com.final_10aeat.common.enumclass.Progress;
import com.final_10aeat.domain.manageArticle.dto.response.SearchManagersManageResponse;
import com.final_10aeat.domain.repairArticle.dto.response.SearchManagerRepairArticleResponseDto;
import com.final_10aeat.global.util.ResponseDTO;
import com.final_10aeat.usecase.SearchArticleUseCase;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/managers")
@RequiredArgsConstructor
@PreAuthorize("hasRole('MANAGER')")
public class ManagerSearchArticleController {

    private final SearchArticleUseCase searchArticleUseCase;

    @GetMapping("/manage/articles")
    public ResponseDTO<CustomPageDto<SearchManagersManageResponse>> managerSearchManageArticle(
        @RequestParam(required = false) String keyword,
        @RequestParam(required = false) Integer year,
        @RequestParam(required = false) Integer month,
        @PageableDefault(size = 5, sort = "id", direction = Direction.DESC) Pageable pageRequest
    ) {
        LocalDateTime now = LocalDateTime.now();
        return ResponseDTO.okWithData(
            PageConverter.convertToCustomPageDto(
                searchArticleUseCase.managerSearchManage(
                    ofNullable(year).orElse(now.getYear()),
                    now, keyword, month, pageRequest
                )
            )
        );
    }

    @GetMapping("/repair/articles")
    public ResponseDTO<CustomPageDto<SearchManagerRepairArticleResponseDto>> managerSearchRepairArticle(
        @RequestParam(required = false) String keyword,
        @RequestParam(required = false) Progress progress,
        @PageableDefault(size = 5, sort = "id", direction = Direction.DESC) Pageable pageRequest
    ) {
        return ResponseDTO.okWithData(
            PageConverter.convertToCustomPageDto(
                searchArticleUseCase.managerSearchRepair(
                    progress, keyword, pageRequest
                )
            )
        );
    }
}

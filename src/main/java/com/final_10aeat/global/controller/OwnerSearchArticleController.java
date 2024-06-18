package com.final_10aeat.global.controller;

import static com.final_10aeat.global.util.PageConverter.convertToCustomPageDto;

import com.final_10aeat.common.dto.PageDto;
import com.final_10aeat.domain.manageArticle.dto.response.ManageArticleListResponseDto;
import com.final_10aeat.domain.repairArticle.dto.response.OwnerRepairArticleResponseDto;
import com.final_10aeat.global.util.ResponseDTO;
import com.final_10aeat.global.usecase.SearchArticleUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@PreAuthorize("hasRole('USER')")
@RequiredArgsConstructor
public class OwnerSearchArticleController {

    private final SearchArticleUseCase searchArticleUseCase;

    @GetMapping("/repair")
    public ResponseDTO<PageDto<OwnerRepairArticleResponseDto>> repairArticleSearch(
        @RequestParam String keyword,
        @PageableDefault(size = 3, sort = "id", direction = Direction.DESC) Pageable pageRequest
    ) {
        return ResponseDTO.okWithData(
            convertToCustomPageDto(
                searchArticleUseCase.repairSearch(keyword, pageRequest)
            )
        );
    }

    @GetMapping("/manage")
    public ResponseDTO<PageDto<ManageArticleListResponseDto>> manageArticleSearch(
        @RequestParam String keyword,
        @PageableDefault(size = 3, sort = "id", direction = Direction.DESC) Pageable pageRequest
    ) {
        return ResponseDTO.okWithData(
            convertToCustomPageDto(
                searchArticleUseCase.manageSearch(keyword, pageRequest)
            )
        );
    }
}

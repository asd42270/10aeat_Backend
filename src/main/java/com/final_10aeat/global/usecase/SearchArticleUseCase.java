package com.final_10aeat.global.usecase;

import com.final_10aeat.common.enumclass.Progress;
import com.final_10aeat.common.service.AuthUserService;
import com.final_10aeat.domain.manageArticle.dto.response.ManageArticleListResponseDto;
import com.final_10aeat.domain.manageArticle.dto.response.SearchManagerManageArticleResponseDto;
import com.final_10aeat.domain.manageArticle.service.ReadManageArticleService;
import com.final_10aeat.domain.repairArticle.dto.response.OwnerRepairArticleResponseDto;
import com.final_10aeat.domain.repairArticle.dto.response.SearchManagerRepairArticleResponseDto;
import com.final_10aeat.domain.repairArticle.service.ManagerArticleListService;
import com.final_10aeat.domain.repairArticle.service.OwnerArticleListService;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SearchArticleUseCase {

    private final AuthUserService authUserService;
    private final ReadManageArticleService readManageArticleService;
    private final OwnerArticleListService ownerArticleListService;
    private final ManagerArticleListService managerArticleListService;

    public Page<OwnerRepairArticleResponseDto> repairSearch(
        String keyword, Pageable pageRequest
    ) {
        Long userOfficeId = authUserService.getUserOfficeId();
        Long memberId = authUserService.getCurrentUserIdAndRole().id();
        return ownerArticleListService.search(userOfficeId, memberId, keyword, pageRequest);

    }

    public Page<ManageArticleListResponseDto> manageSearch(
        String keyword, Pageable pageRequest
    ) {
        Long userOfficeId = authUserService.getUserOfficeId();
        return readManageArticleService.search(userOfficeId, keyword, pageRequest);
    }

    public Page<SearchManagerManageArticleResponseDto> managerSearchManage(
        Integer year, LocalDateTime now, String keyword, Integer month, Pageable pageRequest
    ) {
        Long userOfficeId = authUserService.getUserOfficeId();
        return readManageArticleService.managerSearch(
            userOfficeId, year, keyword, month, pageRequest, now
        );
    }

    public Page<SearchManagerRepairArticleResponseDto> managerSearchRepair(
        Progress progress, String keyword, Pageable pageRequest
    ) {
        Long userOfficeId = authUserService.getUserOfficeId();
        return managerArticleListService.repairSearch(
            userOfficeId, keyword, progress, pageRequest
        );
    }
}

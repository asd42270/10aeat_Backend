package com.final_10aeat.usecase;

import com.final_10aeat.common.service.AuthenticationService;
import com.final_10aeat.domain.manageArticle.dto.response.ListManageArticleResponse;
import com.final_10aeat.domain.manageArticle.dto.response.SearchManagersManageResponse;
import com.final_10aeat.domain.manageArticle.service.ReadManageArticleService;
import com.final_10aeat.domain.repairArticle.dto.response.OwnerRepairArticleResponseDto;
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

    private final AuthenticationService authenticationService;
    private final ReadManageArticleService readManageArticleService;
    private final OwnerArticleListService ownerArticleListService;

    public Page<OwnerRepairArticleResponseDto> repairSearch(
        String keyword, Pageable pageRequest
    ) {
        Long userOfficeId = authenticationService.getUserOfficeId();
        Long memberId = authenticationService.getCurrentUserIdAndRole().id();
        return ownerArticleListService.search(userOfficeId, memberId, keyword, pageRequest);

    }

    public Page<ListManageArticleResponse> manageSearch(
        String keyword, Pageable pageRequest
    ) {
        Long userOfficeId = authenticationService.getUserOfficeId();
        return readManageArticleService.search(userOfficeId, keyword, pageRequest);
    }

    public Page<SearchManagersManageResponse> managerSearchManage(
        Integer year, LocalDateTime now, String keyword, Integer month, Pageable pageRequest
    ) {
        Long userOfficeId = authenticationService.getUserOfficeId();
        return readManageArticleService.managerSearch(
            userOfficeId, year, keyword, month, pageRequest, now
        );
    }
}

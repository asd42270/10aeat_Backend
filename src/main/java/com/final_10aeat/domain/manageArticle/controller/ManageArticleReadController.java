package com.final_10aeat.domain.manageArticle.controller;


import com.final_10aeat.common.service.AuthenticationService;
import com.final_10aeat.domain.manageArticle.dto.response.DetailManageArticleResponse;
import com.final_10aeat.domain.manageArticle.dto.response.ListManageArticleResponse;
import com.final_10aeat.domain.manageArticle.dto.response.SummaryManageArticleResponse;
import com.final_10aeat.domain.manageArticle.service.ReadManageArticleService;
import com.final_10aeat.global.util.ResponseDTO;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@PreAuthorize("hasRole('USER')")
@RequestMapping("/manage/articles")
public class ManageArticleReadController {

    private final ReadManageArticleService readManageArticleService;
    private final AuthenticationService authenticationService;

    @GetMapping("/summary")
    public ResponseDTO<SummaryManageArticleResponse> summary() {
        Long userOfficeId = authenticationService.getUserOfficeId();

        return ResponseDTO.okWithData(
            readManageArticleService.summary(userOfficeId)
        );
    }

    @GetMapping("/{manageArticleId}")
    public ResponseDTO<DetailManageArticleResponse> detailArticle(@PathVariable Long manageArticleId) {
        Long userOfficeId = authenticationService.getUserOfficeId();
        return ResponseDTO.okWithData(
            readManageArticleService.detailArticle(userOfficeId, manageArticleId)
        );
    }

    @GetMapping("/list")
    public ResponseDTO<List<ListManageArticleResponse>> listArticle(){
        Long userOfficeId = authenticationService.getUserOfficeId();

        return ResponseDTO.okWithData(
            readManageArticleService.listArticle(userOfficeId)
        );
    }
}

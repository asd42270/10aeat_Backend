package com.final_10aeat.domain.manageArticle.controller;

import com.final_10aeat.domain.manageArticle.dto.request.CreateManageArticleRequestDto;
import com.final_10aeat.domain.manageArticle.service.ManagerManageArticleService;
import com.final_10aeat.global.security.principal.ManagerPrincipal;
import com.final_10aeat.global.util.ResponseDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/managers/manage/articles")
public class ManagerManageArticleController {

    private final ManagerManageArticleService managerManageArticleService;

    @PostMapping
    public ResponseDTO<Void> create(@RequestBody @Valid CreateManageArticleRequestDto request) {
        ManagerPrincipal principal = (ManagerPrincipal) SecurityContextHolder.getContext()
            .getAuthentication().getPrincipal();

        managerManageArticleService.create(request, principal.getManager());

        return ResponseDTO.ok();
    }
}

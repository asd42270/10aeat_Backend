package com.final_10aeat.domain.manageArticle.controller;

import com.final_10aeat.domain.manageArticle.dto.request.CreateManageArticleRequestDto;
import com.final_10aeat.domain.manageArticle.dto.request.UpdateManageArticleRequestDto;
import com.final_10aeat.domain.manageArticle.service.ManagerManageArticleService;
import com.final_10aeat.global.security.principal.ManagerPrincipal;
import com.final_10aeat.global.util.ResponseDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@PreAuthorize("hasRole('MANAGER')")
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

    @PatchMapping("/{manageArticleId}")
    public ResponseDTO<Void> update(
        @RequestBody UpdateManageArticleRequestDto request,
        @PathVariable Long manageArticleId
    ) {
        ManagerPrincipal principal = (ManagerPrincipal) SecurityContextHolder.getContext()
            .getAuthentication().getPrincipal();

        managerManageArticleService.update(manageArticleId, request, principal.getManager());

        return ResponseDTO.ok();
    }

    @DeleteMapping("/{manageArticleId}")
    public ResponseDTO<Void> delete(
        @PathVariable Long manageArticleId
    ){
        ManagerPrincipal principal = (ManagerPrincipal) SecurityContextHolder.getContext()
            .getAuthentication().getPrincipal();

        managerManageArticleService.delete(manageArticleId, principal.getManager());

        return ResponseDTO.ok();
    }
}

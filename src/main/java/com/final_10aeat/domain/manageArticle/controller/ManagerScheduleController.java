package com.final_10aeat.domain.manageArticle.controller;

import com.final_10aeat.domain.manageArticle.dto.request.ScheduleRequestDto;
import com.final_10aeat.domain.manageArticle.service.ManageScheduleService;
import com.final_10aeat.global.security.principal.ManagerPrincipal;
import com.final_10aeat.global.util.ResponseDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@PreAuthorize("hasRole('MANAGER')")
@RequestMapping("/managers/manage/articles/schedules")
public class ManagerScheduleController {

    private final ManageScheduleService manageScheduleService;

    @PostMapping("/{manageArticleId}")
    public ResponseDTO<Void> register(
        @PathVariable Long manageArticleId,
        @RequestBody @Valid ScheduleRequestDto request
    ) {
        ManagerPrincipal principal = (ManagerPrincipal) SecurityContextHolder.getContext()
            .getAuthentication().getPrincipal();

        manageScheduleService.register(manageArticleId, request, principal.getManager());
        return ResponseDTO.ok();
    }

    @PostMapping("/{manageArticleId}}/{manageScheduleId}")
    public ResponseDTO<Void> complete(
        // TODO 메소드 명이 직관적이지 않아 보임, 더 좋은 이름 있으면 변경하기
        // 엔티티의 메소드와 이름이 같으나 서로 다른 처리라서 좋지 않아 보임
        @PathVariable Long manageArticleId,
        @PathVariable Long manageScheduleId
    ) {
        ManagerPrincipal principal = (ManagerPrincipal) SecurityContextHolder.getContext()
            .getAuthentication().getPrincipal();

        manageScheduleService.complete(manageArticleId, manageScheduleId, principal.getManager());

        return ResponseDTO.ok();
    }
}

package com.final_10aeat.domain.manageArticle.controller;

import com.final_10aeat.domain.manageArticle.dto.request.CreateScheduleRequestDto;
import com.final_10aeat.domain.manageArticle.service.ManagerScheduleService;
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
@RequestMapping("/managers/manage/articles/schedules")
public class ManagerScheduleController {

    private final ManagerScheduleService managerScheduleService;

    @PostMapping("/{manageArticleId}")
    public ResponseDTO<Void> register(
        @PathVariable Long manageArticleId,
        @RequestBody @Valid CreateScheduleRequestDto request
    ) {
        ManagerPrincipal principal = (ManagerPrincipal) SecurityContextHolder.getContext()
            .getAuthentication().getPrincipal();

        managerScheduleService.register(manageArticleId, request, principal.getManager());
        return ResponseDTO.ok();
    }

    @PostMapping("/progress/{manageScheduleId}")
    public ResponseDTO<Void> complete(
        @PathVariable Long manageScheduleId
    ) {
        ManagerPrincipal principal = (ManagerPrincipal) SecurityContextHolder.getContext()
            .getAuthentication().getPrincipal();

        managerScheduleService.complete(manageScheduleId, principal.getManager());

        return ResponseDTO.ok();
    }

    @PatchMapping("/progress/{manageScheduleId}")
    public ResponseDTO<Void> update(
        @PathVariable Long manageScheduleId,
        @RequestBody @Valid CreateScheduleRequestDto request
    ) {
        ManagerPrincipal principal = (ManagerPrincipal) SecurityContextHolder.getContext()
            .getAuthentication().getPrincipal();

        managerScheduleService.update(manageScheduleId, request, principal.getManager()
        );

        return ResponseDTO.ok();
    }

    @DeleteMapping("/progress/{manageScheduleId}")
    public ResponseDTO<Void> delete(
        @PathVariable Long manageScheduleId
    ) {
        ManagerPrincipal principal = (ManagerPrincipal) SecurityContextHolder.getContext()
            .getAuthentication().getPrincipal();

        managerScheduleService.delete(manageScheduleId, principal.getManager());

        return ResponseDTO.ok();
    }
}

package com.final_10aeat.domain.repairArticle.controller;

import com.final_10aeat.domain.repairArticle.dto.request.CreateCustomProgressRequestDto;
import com.final_10aeat.domain.repairArticle.dto.request.CreateRepairArticleRequestDto;
import com.final_10aeat.domain.repairArticle.dto.request.UpdateCustomProgressRequestDto;
import com.final_10aeat.domain.repairArticle.dto.request.UpdateRepairArticleRequestDto;
import com.final_10aeat.domain.repairArticle.service.ManagerRepairArticleService;
import com.final_10aeat.global.security.principal.ManagerPrincipal;
import com.final_10aeat.global.util.ResponseDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
@RequestMapping("/managers/repair/article")
public class ManagerRepairArticleController {

    private final ManagerRepairArticleService managerRepairArticleService;

    @PostMapping
    public ResponseEntity<ResponseDTO<Void>> createRepairArticle(@RequestBody @Valid
    CreateRepairArticleRequestDto request) {
        ManagerPrincipal principal = (ManagerPrincipal) SecurityContextHolder.getContext()
            .getAuthentication().getPrincipal();
        managerRepairArticleService.createRepairArticle(request, principal.getManager().getId());
        return ResponseEntity.ok(ResponseDTO.ok());
    }

    @DeleteMapping("/{repairArticleId}")
    public ResponseEntity<ResponseDTO<Void>> deleteRepairArticleById(
        @PathVariable Long repairArticleId) {
        ManagerPrincipal principal = (ManagerPrincipal) SecurityContextHolder.getContext()
            .getAuthentication().getPrincipal();
        managerRepairArticleService.deleteRepairArticleById(repairArticleId,
            principal.getManager().getId());
        return ResponseEntity.ok(ResponseDTO.ok());
    }

    @PatchMapping("/{repairArticleId}")
    public ResponseEntity<ResponseDTO<Void>> updateRepairArticleById(
        @PathVariable Long repairArticleId,
        @RequestBody UpdateRepairArticleRequestDto request) {
        ManagerPrincipal principal = (ManagerPrincipal) SecurityContextHolder.getContext()
            .getAuthentication().getPrincipal();
        managerRepairArticleService.updateRepairArticle(repairArticleId, request,
            principal.getManager().getId());
        return ResponseEntity.ok(ResponseDTO.ok());
    }

    @PostMapping("/progress/{repairArticleId}")
    public ResponseEntity<ResponseDTO<Void>> createCustomProgress(
        @PathVariable Long repairArticleId,
        @RequestBody @Valid CreateCustomProgressRequestDto request) {
        ManagerPrincipal principal = (ManagerPrincipal) SecurityContextHolder.getContext()
            .getAuthentication().getPrincipal();
        managerRepairArticleService.createCustomProgress(repairArticleId,
            principal.getManager().getId(), request);
        return ResponseEntity.ok(ResponseDTO.ok());
    }

    @PatchMapping("/progress/{progressId}")
    public ResponseEntity<ResponseDTO<Void>> updateCustomProgress(@PathVariable Long progressId,
        @RequestBody @Valid UpdateCustomProgressRequestDto request) {
        ManagerPrincipal principal = (ManagerPrincipal) SecurityContextHolder.getContext()
            .getAuthentication().getPrincipal();
        managerRepairArticleService.updateCustomProgress(progressId, principal.getManager().getId(),
            request);
        return ResponseEntity.ok(ResponseDTO.ok());
    }
}

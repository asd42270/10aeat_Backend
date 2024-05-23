package com.final_10aeat.domain.repairArticle.controller;

import com.final_10aeat.domain.repairArticle.dto.request.CreateRepairArticleRequestDto;
import com.final_10aeat.domain.repairArticle.dto.request.UpdateRepairArticleRequestDto;
import com.final_10aeat.domain.repairArticle.service.RepairArticleService;
import com.final_10aeat.global.security.principal.ManagerPrincipal;
import com.final_10aeat.global.util.ResponseDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
public class CreateRepairArticleController {

    private final RepairArticleService repairArticleService;

    @PostMapping
    public ResponseEntity<ResponseDTO<Void>> createRepairArticle(@RequestBody @Valid
    CreateRepairArticleRequestDto request,
        @AuthenticationPrincipal ManagerPrincipal managerPrincipal) {
        repairArticleService.createRepairArticle(request, managerPrincipal.getManager().getId());
        return ResponseEntity.ok(ResponseDTO.ok());
    }

    @DeleteMapping("/{repairArticleId}")
    public ResponseEntity<ResponseDTO<Void>> deleteRepairArticleById(
        @PathVariable Long repairArticleId,
        @AuthenticationPrincipal ManagerPrincipal managerPrincipal) {
        repairArticleService.deleteRepairArticleById(repairArticleId,
            managerPrincipal.getManager().getId());
        return ResponseEntity.ok(ResponseDTO.ok());
    }

    @PatchMapping("/{repairArticleId}")
    public ResponseEntity<ResponseDTO<Void>> updateRepairArticleById(
        @PathVariable Long repairArticleId,
        @RequestBody UpdateRepairArticleRequestDto request,
        @AuthenticationPrincipal ManagerPrincipal managerPrincipal) {
        repairArticleService.updateRepairArticle(repairArticleId, request,
            managerPrincipal.getManager().getId());
        return ResponseEntity.ok(ResponseDTO.ok());
    }
}

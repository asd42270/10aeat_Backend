package com.final_10aeat.domain.repairArticle.controller;

import com.final_10aeat.domain.repairArticle.dto.request.CreateRepairArticleRequestDto;
import com.final_10aeat.domain.repairArticle.service.RepairArticleService;
import com.final_10aeat.global.security.principal.ManagerPrincipal;
import com.final_10aeat.global.util.ResponseDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/managers/repair/article")
public class RepairArticleController {

    private final RepairArticleService repairArticleService;

    @PostMapping
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<ResponseDTO<Void>> createRepairArticle(@RequestBody @Valid
        CreateRepairArticleRequestDto request,@AuthenticationPrincipal ManagerPrincipal managerPrincipal){
        repairArticleService.createRepairArticle(request, managerPrincipal.getManager().getId());
        return ResponseEntity.ok(ResponseDTO.ok());
    }
}

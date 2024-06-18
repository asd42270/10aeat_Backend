package com.final_10aeat.domain.save.controller;

import com.final_10aeat.domain.save.service.SavedArticleService;
import com.final_10aeat.global.security.principal.MemberPrincipal;
import com.final_10aeat.global.util.ResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@PreAuthorize("hasRole('USER')")
@RequestMapping("/repair/articles/save")
public class SavedArticleController {

    private final SavedArticleService savedArticleService;

    @PostMapping("/{repairArticleId}")
    public ResponseEntity<ResponseDTO<Void>> saveArticle(@PathVariable Long repairArticleId) {
        MemberPrincipal principal = (MemberPrincipal) SecurityContextHolder.getContext()
            .getAuthentication().getPrincipal();
        Long memberId = principal.getMember().getId();
        savedArticleService.saveArticle(repairArticleId, memberId);
        return ResponseEntity.ok(ResponseDTO.ok());
    }

    @DeleteMapping("/{repairArticleId}")
    public ResponseEntity<ResponseDTO<Void>> unsaveArticle(@PathVariable Long repairArticleId) {
        MemberPrincipal principal = (MemberPrincipal) SecurityContextHolder.getContext()
            .getAuthentication().getPrincipal();
        Long memberId = principal.getMember().getId();
        savedArticleService.unsaveArticle(repairArticleId, memberId);
        return ResponseEntity.ok(ResponseDTO.ok());
    }
}

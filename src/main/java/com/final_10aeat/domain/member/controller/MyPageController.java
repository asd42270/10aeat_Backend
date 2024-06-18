package com.final_10aeat.domain.member.controller;

import com.final_10aeat.domain.member.dto.request.BuildingInfoRequestDto;
import com.final_10aeat.domain.member.dto.response.MyBuildingInfoResponseDto;
import com.final_10aeat.domain.member.dto.response.MyCommentResponseDto;
import com.final_10aeat.domain.member.dto.response.MyInfoResponseDto;
import com.final_10aeat.domain.member.dto.response.MySavedArticleResponseDto;
import com.final_10aeat.domain.member.service.MyPageService;
import com.final_10aeat.global.security.principal.MemberPrincipal;
import com.final_10aeat.global.util.ResponseDTO;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/my")
@RequiredArgsConstructor
@PreAuthorize("hasRole('USER')")
public class MyPageController {

    private final MyPageService myPageService;

    @GetMapping("/building/units")
    public ResponseEntity<ResponseDTO<List<MyBuildingInfoResponseDto>>> getBuildingInfo() {
        MemberPrincipal principal = (MemberPrincipal) SecurityContextHolder.getContext()
            .getAuthentication().getPrincipal();
        return ResponseEntity.ok(
            ResponseDTO.okWithData(myPageService.getBuildingInfo(principal.getMember())));
    }

    @GetMapping("/info")
    public ResponseEntity<ResponseDTO<MyInfoResponseDto>> getMyInfo() {
        MemberPrincipal principal = (MemberPrincipal) SecurityContextHolder.getContext()
            .getAuthentication().getPrincipal();
        MyInfoResponseDto myInfo = myPageService.getMyInfo(principal.getMember());
        return ResponseEntity.ok(ResponseDTO.okWithData(myInfo));
    }

    @PostMapping("/building/units")
    public ResponseEntity<ResponseDTO<Void>> addBuildingInfo(
        @RequestBody @Valid BuildingInfoRequestDto requestDto) {
        MemberPrincipal principal = (MemberPrincipal) SecurityContextHolder.getContext()
            .getAuthentication().getPrincipal();
        myPageService.addBuildingInfo(principal.getMember(), requestDto);
        return ResponseEntity.ok(ResponseDTO.ok());
    }

    @DeleteMapping("/building/units/{buildingInfoId}")
    public ResponseEntity<ResponseDTO<Void>> removeBuildingInfo(@PathVariable Long buildingInfoId) {
        MemberPrincipal principal = (MemberPrincipal) SecurityContextHolder.getContext()
            .getAuthentication().getPrincipal();
        myPageService.removeBuildingInfo(principal.getMember(), buildingInfoId);
        return ResponseEntity.ok(ResponseDTO.ok());
    }

    @GetMapping("/comments")
    public ResponseEntity<ResponseDTO<List<MyCommentResponseDto>>> getMyComments() {
        MemberPrincipal principal = (MemberPrincipal) SecurityContextHolder.getContext()
            .getAuthentication().getPrincipal();
        List<MyCommentResponseDto> comments = myPageService.getMyComments(principal.getMember());
        return ResponseEntity.ok(ResponseDTO.okWithData(comments));
    }

    @GetMapping("/saved-articles")
    public ResponseEntity<ResponseDTO<List<MySavedArticleResponseDto>>> getMySavedArticles() {
        MemberPrincipal principal = (MemberPrincipal) SecurityContextHolder.getContext()
            .getAuthentication().getPrincipal();
        List<MySavedArticleResponseDto> savedArticles = myPageService.getMySavedArticles(
            principal.getMember().getId());
        return ResponseEntity.ok(ResponseDTO.okWithData(savedArticles));
    }
}

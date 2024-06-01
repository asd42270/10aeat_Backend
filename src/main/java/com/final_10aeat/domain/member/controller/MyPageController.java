package com.final_10aeat.domain.member.controller;

import com.final_10aeat.domain.member.dto.request.BuildingInfoRequestDto;
import com.final_10aeat.domain.member.dto.response.MyBuildingInfoResponseDto;
import com.final_10aeat.domain.member.dto.response.MyInfoResponseDto;
import com.final_10aeat.domain.member.service.MyPageService;
import com.final_10aeat.global.security.principal.MemberPrincipal;
import com.final_10aeat.global.util.ResponseDTO;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
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
    public ResponseDTO<List<MyBuildingInfoResponseDto>> getBuildingInfo() {

        MemberPrincipal principal = (MemberPrincipal) SecurityContextHolder.getContext()
            .getAuthentication().getPrincipal();

        return ResponseDTO.okWithData(myPageService.getBuildingInfo(principal.getMember()));
    }

    @GetMapping("/info")
    public ResponseDTO<MyInfoResponseDto> getMyInfo() {
        MemberPrincipal principal = (MemberPrincipal) SecurityContextHolder.getContext()
            .getAuthentication().getPrincipal();
        MyInfoResponseDto myInfo = myPageService.getMyInfo(principal.getMember());
        return ResponseDTO.okWithData(myInfo);
    }

    @PostMapping("/building/units")
    public ResponseDTO<Void> addBuildingInfo(@RequestBody BuildingInfoRequestDto requestDto) {
        MemberPrincipal principal = (MemberPrincipal) SecurityContextHolder.getContext()
            .getAuthentication().getPrincipal();
        myPageService.addBuildingInfo(principal.getMember(), requestDto);
        return ResponseDTO.ok();
    }
}

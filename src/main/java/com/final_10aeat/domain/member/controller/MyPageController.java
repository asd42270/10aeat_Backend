package com.final_10aeat.domain.member.controller;


import com.final_10aeat.domain.member.dto.response.MyBuildingInfoResponseDto;
import com.final_10aeat.domain.member.service.MyPageService;
import com.final_10aeat.global.security.principal.MemberPrincipal;
import com.final_10aeat.global.util.ResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/my")
@RequiredArgsConstructor
@PreAuthorize("hasRole('USER')")
public class MyPageController {

    private final MyPageService buildingInfoService;

    @GetMapping("/building/units")
    public ResponseDTO<List<MyBuildingInfoResponseDto>> getBuildingInfo() {

        MemberPrincipal principal = (MemberPrincipal) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();

        return ResponseDTO.okWithData(buildingInfoService.getBuildingInfo(principal.getMember()));
    }
}

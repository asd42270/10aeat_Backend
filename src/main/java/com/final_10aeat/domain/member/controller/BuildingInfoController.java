package com.final_10aeat.domain.member.controller;


import com.final_10aeat.domain.member.dto.response.BuildingInfoResponseDto;
import com.final_10aeat.domain.member.service.BuildingInfoService;
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
@RequestMapping("/members/building-info")
@RequiredArgsConstructor
@PreAuthorize("hasRole('USER')")
public class BuildingInfoController {

    private final BuildingInfoService buildingInfoService;

    @GetMapping()
    public ResponseDTO<List<BuildingInfoResponseDto>> getBuildingInfo() {

        MemberPrincipal principal = (MemberPrincipal) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();

        return ResponseDTO.okWithData(buildingInfoService.getBuildingInfo(principal.getMember()));
    }
}

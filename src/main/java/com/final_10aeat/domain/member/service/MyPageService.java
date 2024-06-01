package com.final_10aeat.domain.member.service;

import com.final_10aeat.domain.member.dto.response.MyBuildingInfoResponseDto;
import com.final_10aeat.domain.member.entity.BuildingInfo;
import com.final_10aeat.domain.member.entity.Member;
import com.final_10aeat.domain.member.exception.UserNotExistException;
import com.final_10aeat.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional
public class MyPageService {


    private final MemberRepository memberRepository;

    @Transactional(readOnly = true)
    public List<MyBuildingInfoResponseDto> getBuildingInfo(Member member) {

        Member loadedMember = memberRepository.findByIdWithBuildingInfos(member.getId())
                .orElseThrow(UserNotExistException::new);

        Set<BuildingInfo> buildingInfos = loadedMember.getBuildingInfos();

        return buildingInfos.stream()
                .sorted()
                .map(this::toInfoDto)
                .toList();
    }

    private MyBuildingInfoResponseDto toInfoDto(BuildingInfo buildingInfo) {
        return MyBuildingInfoResponseDto.builder()
                .officeName(buildingInfo.getOffice().getOfficeName())
                .buildingInfoId(buildingInfo.getId())
                .dong(buildingInfo.getDong())
                .ho(buildingInfo.getHo())
                .build();
    }
}

package com.final_10aeat.domain.member.service;

import com.final_10aeat.domain.comment.repository.CommentRepository;
import com.final_10aeat.domain.member.dto.request.BuildingInfoRequestDto;
import com.final_10aeat.domain.member.dto.response.MyBuildingInfoResponseDto;
import com.final_10aeat.domain.member.dto.response.MyCommentsResponseDto;
import com.final_10aeat.domain.member.dto.response.MyInfoResponseDto;
import com.final_10aeat.domain.member.dto.response.MySaveResponseDto;
import com.final_10aeat.domain.member.entity.BuildingInfo;
import com.final_10aeat.domain.member.entity.Member;
import com.final_10aeat.domain.member.exception.BuildingInfoNotAssociatedException;
import com.final_10aeat.domain.member.exception.BuildingInfoNotFound;
import com.final_10aeat.domain.member.exception.DuplicateBuildingInfoException;
import com.final_10aeat.domain.member.exception.MinBuildingInfoRequiredException;
import com.final_10aeat.domain.member.exception.UserNotExistException;
import com.final_10aeat.domain.member.repository.BuildingInfoRepository;
import com.final_10aeat.domain.member.repository.MemberRepository;
import com.final_10aeat.domain.office.entity.Office;
import com.final_10aeat.domain.office.exception.OfficeNotFoundException;
import com.final_10aeat.domain.office.repository.OfficeRepository;
import com.final_10aeat.domain.save.repository.ArticleSaveRepository;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MyPageService {

    private final MemberRepository memberRepository;
    private final OfficeRepository officeRepository;
    private final BuildingInfoRepository buildingInfoRepository;
    private final CommentRepository commentRepository;
    private final ArticleSaveRepository articleSaveRepository;

    public List<MyBuildingInfoResponseDto> getBuildingInfo(Member member) {
        Member loadedMember = findMemberByIdWithBuildingInfos(member.getId());
        Set<BuildingInfo> buildingInfos = loadedMember.getBuildingInfos();
        Long defaultOfficeId = member.getDefaultOffice();

        return buildingInfos.stream()
            .filter(bi -> bi.getOffice() != null && bi.getOffice().getId().equals(defaultOfficeId))
            .sorted(Comparator.comparing(bi -> bi.getOffice().getOfficeName()))
            .map(this::toInfoDto)
            .collect(Collectors.toList());
    }

    public MyInfoResponseDto getMyInfo(Member member) {
        Office office = officeRepository.findById(member.getDefaultOffice())
            .orElseThrow(OfficeNotFoundException::new);

        return new MyInfoResponseDto(
            member.getName(),
            member.getRole().name(),
            office.getOfficeName()
        );
    }

    @Transactional
    public void addBuildingInfo(Member member, BuildingInfoRequestDto requestDto) {
        Long defaultOfficeId = member.getDefaultOffice();
        if (defaultOfficeId == null) {
            throw new OfficeNotFoundException();
        }

        Office office = officeRepository.findById(defaultOfficeId)
            .orElseThrow(OfficeNotFoundException::new);

        Member managedMember = findMemberByIdWithBuildingInfos(member.getId());

        Set<BuildingInfo> buildingInfos = managedMember.getBuildingInfos();
        if (buildingInfos.stream().anyMatch(
            info -> info.getDong().equals(requestDto.dong()) && info.getHo()
                .equals(requestDto.ho()))) {
            throw new DuplicateBuildingInfoException();
        }

        BuildingInfo buildingInfo = BuildingInfo.builder()
            .dong(requestDto.dong())
            .ho(requestDto.ho())
            .office(office)
            .build();

        buildingInfoRepository.save(buildingInfo);

        buildingInfos.add(buildingInfo);

        managedMember.setBuildingInfos(buildingInfos);
        memberRepository.save(managedMember);
    }

    @Transactional
    public void removeBuildingInfo(Member member, Long buildingInfoId) {
        Member managedMember = findMemberByIdWithBuildingInfos(member.getId());

        Set<BuildingInfo> buildingInfos = managedMember.getBuildingInfos();
        if (buildingInfos.size() <= 1) {
            throw new MinBuildingInfoRequiredException();
        }

        BuildingInfo buildingInfoToRemove = buildingInfoRepository.findById(buildingInfoId)
            .orElseThrow(BuildingInfoNotFound::new);

        if (!buildingInfos.contains(buildingInfoToRemove)) {
            throw new BuildingInfoNotAssociatedException();
        }

        buildingInfos.remove(buildingInfoToRemove);
        managedMember.setBuildingInfos(buildingInfos);
        memberRepository.save(managedMember);
    }

    public List<MyCommentsResponseDto> getMyComments(Member member) {
        return commentRepository.findByMemberAndRepairArticleOfficeId(member,
                member.getDefaultOffice())
            .stream()
            .map(comment -> new MyCommentsResponseDto(
                comment.getRepairArticle().getId(),
                comment.getContent(),
                comment.getCreatedAt(),
                comment.getMember().getName()
            ))
            .collect(Collectors.toList());
    }

    public List<MySaveResponseDto> getMySavedArticles(Long memberId) {
        Member member = findMemberByIdWithBuildingInfos(memberId);
        Long defaultOfficeId = member.getDefaultOffice();

        return articleSaveRepository.findByMemberAndOffice(member, defaultOfficeId)
            .stream()
            .map(articleSave -> new MySaveResponseDto(
                articleSave.getRepairArticle().getId(),
                articleSave.getRepairArticle().getTitle(),
                articleSave.getRepairArticle().getCreatedAt(),
                articleSave.getRepairArticle().getManager().getName()
            ))
            .collect(Collectors.toList());
    }

    private Member findMemberByIdWithBuildingInfos(Long memberId) {
        return memberRepository.findMemberByIdWithBuildingInfos(memberId)
            .orElseThrow(UserNotExistException::new);
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

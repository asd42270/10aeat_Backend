package com.final_10aeat.domain.save.unit;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.final_10aeat.common.enumclass.MemberRole;
import com.final_10aeat.domain.member.dto.request.BuildingInfoRequestDto;
import com.final_10aeat.domain.member.entity.BuildingInfo;
import com.final_10aeat.domain.member.entity.Member;
import com.final_10aeat.domain.member.exception.BuildingInfoNotAssociatedException;
import com.final_10aeat.domain.member.exception.BuildingInfoNotFound;
import com.final_10aeat.domain.member.exception.DuplicateBuildingInfoException;
import com.final_10aeat.domain.member.exception.MinBuildingInfoRequiredException;
import com.final_10aeat.domain.member.exception.UserNotExistException;
import com.final_10aeat.domain.member.repository.BuildingInfoRepository;
import com.final_10aeat.domain.member.repository.MemberRepository;
import com.final_10aeat.domain.member.service.MyPageService;
import com.final_10aeat.domain.office.entity.Office;
import com.final_10aeat.domain.office.exception.OfficeNotFoundException;
import com.final_10aeat.domain.office.repository.OfficeRepository;
import java.util.HashSet;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class MyPageServiceTest {

    @Mock
    private MemberRepository memberRepository;
    @Mock
    private OfficeRepository officeRepository;
    @Mock
    private BuildingInfoRepository buildingInfoRepository;

    @InjectMocks
    private MyPageService myPageService;

    private Member member;
    private Office office;
    private final Long buildingInfoId = 1L;
    private final BuildingInfoRequestDto buildingInfoRequestDto = new BuildingInfoRequestDto("101동",
        "202호");

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        office = Office.builder()
            .id(1L)
            .officeName("갑을그레이트벨리")
            .build();

        member = Member.builder()
            .id(1L)
            .name("홍길동")
            .defaultOffice(1L)
            .buildingInfos(new HashSet<>())
            .role(MemberRole.OWNER)
            .build();

        BuildingInfo buildingInfo1 = BuildingInfo.builder()
            .id(1L)
            .dong("101동")
            .ho("201호")
            .office(office)
            .build();

        BuildingInfo buildingInfo2 = BuildingInfo.builder()
            .id(2L)
            .dong("102동")
            .ho("202호")
            .office(office)
            .build();

        member.getBuildingInfos().add(buildingInfo1);
        member.getBuildingInfos().add(buildingInfo2);
    }

    @Nested
    @DisplayName("getBuildingInfo()는")
    class GetBuildingInfo {

        @Test
        @DisplayName("성공적으로 건물 정보를 조회한다.")
        void willSuccess() {
            when(memberRepository.findMemberByIdWithBuildingInfos(anyLong())).thenReturn(
                Optional.of(member));

            assertDoesNotThrow(() -> myPageService.getBuildingInfo(member));
            verify(memberRepository).findMemberByIdWithBuildingInfos(anyLong());
        }

        @Test
        @DisplayName("사용자가 존재하지 않는 경우 예외를 발생시킨다.")
        void NotFound_willFail() {
            when(memberRepository.findMemberByIdWithBuildingInfos(anyLong())).thenReturn(
                Optional.empty());

            assertThrows(UserNotExistException.class, () -> myPageService.getBuildingInfo(member));
            verify(memberRepository).findMemberByIdWithBuildingInfos(anyLong());
        }
    }

    @Nested
    @DisplayName("getMyInfo()는")
    class GetMyInfo {

        @Test
        @DisplayName("성공적으로 사용자의 정보를 조회한다.")
        void willSuccess() {
            when(officeRepository.findById(anyLong())).thenReturn(Optional.of(office));

            assertDoesNotThrow(() -> myPageService.getMyInfo(member));
            verify(officeRepository).findById(anyLong());
        }

        @Test
        @DisplayName("사무소가 존재하지 않는 경우 예외를 발생시킨다.")
        void OfficeNotFound_willFail() {
            when(officeRepository.findById(anyLong())).thenReturn(Optional.empty());

            assertThrows(OfficeNotFoundException.class, () -> myPageService.getMyInfo(member));
            verify(officeRepository).findById(anyLong());
        }
    }

    @Nested
    @DisplayName("addBuildingInfo()는")
    class AddBuildingInfo {

        @Test
        @DisplayName("성공적으로 건물 정보를 추가한다.")
        void willSuccess() {
            when(officeRepository.findById(anyLong())).thenReturn(Optional.of(office));
            when(memberRepository.findById(anyLong())).thenReturn(Optional.of(member));

            assertDoesNotThrow(() -> myPageService.addBuildingInfo(member, buildingInfoRequestDto));
            verify(officeRepository).findById(anyLong());
            verify(memberRepository).findById(anyLong());
        }

        @Test
        @DisplayName("중복된 건물 정보를 추가하려는 경우 예외를 발생시킨다.")
        void DuplicateBuildingInfo_willFail() {
            BuildingInfo buildingInfo = BuildingInfo.builder()
                .dong("101동")
                .ho("202호")
                .office(office)
                .build();

            member.getBuildingInfos().add(buildingInfo);

            when(officeRepository.findById(anyLong())).thenReturn(Optional.of(office));
            when(memberRepository.findById(anyLong())).thenReturn(Optional.of(member));

            assertThrows(DuplicateBuildingInfoException.class,
                () -> myPageService.addBuildingInfo(member, buildingInfoRequestDto));
            verify(officeRepository).findById(anyLong());
            verify(memberRepository).findById(anyLong());
        }

        @Test
        @DisplayName("사무소가 존재하지 않는 경우 예외를 발생시킨다.")
        void OfficeNotFound_willFail() {
            when(officeRepository.findById(anyLong())).thenReturn(Optional.empty());

            assertThrows(OfficeNotFoundException.class,
                () -> myPageService.addBuildingInfo(member, buildingInfoRequestDto));
            verify(officeRepository).findById(anyLong());
        }
    }

    @Nested
    @DisplayName("removeBuildingInfo()는")
    class RemoveBuildingInfo {

        @Test
        @DisplayName("성공적으로 건물 정보를 삭제한다.")
        void willSuccess() {
            BuildingInfo buildingInfo = BuildingInfo.builder()
                .id(buildingInfoId)
                .dong("101동")
                .ho("202호")
                .office(office)
                .build();

            member.getBuildingInfos().add(buildingInfo);
            when(memberRepository.findById(anyLong())).thenReturn(Optional.of(member));
            when(buildingInfoRepository.findById(anyLong())).thenReturn(Optional.of(buildingInfo));

            assertDoesNotThrow(() -> myPageService.removeBuildingInfo(member, 2L));
            verify(memberRepository).findById(anyLong());
            verify(buildingInfoRepository).findById(anyLong());
        }

        @Test
        @DisplayName("삭제할 건물 정보가 존재하지 않는 경우 예외를 발생시킨다.")
        void NotFound_willFail() {
            when(memberRepository.findById(anyLong())).thenReturn(Optional.of(member));
            when(buildingInfoRepository.findById(anyLong())).thenReturn(Optional.empty());

            assertThrows(BuildingInfoNotFound.class,
                () -> myPageService.removeBuildingInfo(member, buildingInfoId));
            verify(memberRepository).findById(anyLong());
            verify(buildingInfoRepository).findById(anyLong());
        }

        @Test
        @DisplayName("사용자가 적어도 하나의 건물 정보를 가지고 있어야 하는 경우 예외를 발생시킨다.")
        void MinBuildingInfoRequired_willFail() {
            BuildingInfo buildingInfo1 = BuildingInfo.builder()
                .id(1L)
                .dong("101동")
                .ho("201호")
                .office(office)
                .build();

            member.getBuildingInfos().clear();
            member.getBuildingInfos().add(buildingInfo1);

            when(memberRepository.findById(anyLong())).thenReturn(Optional.of(member));
            when(buildingInfoRepository.findById(anyLong())).thenReturn(Optional.of(buildingInfo1));

            assertThrows(MinBuildingInfoRequiredException.class,
                () -> myPageService.removeBuildingInfo(member, buildingInfo1.getId()));
            verify(memberRepository).findById(anyLong());
        }

        @Test
        @DisplayName("삭제할 건물 정보가 사용자의 건물 정보에 포함되지 않는 경우 예외를 발생시킨다.")
        void BuildingInfoNotAssociated_willFail() {
            BuildingInfo buildingInfo = BuildingInfo.builder()
                .id(1L)
                .dong("101동")
                .ho("202호")
                .office(office)
                .build();

            when(memberRepository.findById(anyLong())).thenReturn(Optional.of(member));
            when(buildingInfoRepository.findById(anyLong())).thenReturn(Optional.of(buildingInfo));

            assertThrows(BuildingInfoNotAssociatedException.class,
                () -> myPageService.removeBuildingInfo(member, buildingInfo.getId()));
            verify(memberRepository).findById(anyLong());
            verify(buildingInfoRepository).findById(anyLong());
        }
    }
}

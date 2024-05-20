package com.final_10aeat.domain.member.unit;

import com.final_10aeat.domain.member.dto.request.MemberRegisterRequestDto;
import com.final_10aeat.domain.member.dto.request.MemberWithdrawRequestDto;
import com.final_10aeat.domain.member.entity.BuildingInfo;
import com.final_10aeat.domain.member.entity.Member;
import com.final_10aeat.domain.member.entity.MemberRole;
import com.final_10aeat.domain.member.exception.MemberMissMatchException;
import com.final_10aeat.domain.member.exception.MemberNotExistException;
import com.final_10aeat.domain.member.repository.BuildingInfoRepository;
import com.final_10aeat.domain.member.repository.MemberRepository;
import com.final_10aeat.domain.member.service.MemberService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.Set;

import static org.mockito.Mockito.*;

public class WithdrawServiceTest {

    @InjectMocks
    private MemberService memberService;
    @Mock
    private MemberRepository memberRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private BuildingInfoRepository buildingInfoRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        String email = "spring";
        String password = "spring";

        BuildingInfo buildingInfo = BuildingInfo.builder()
                .dong("102동")
                .ho("2212호")
                .office(null)
                .build();

        when(buildingInfoRepository.save(any(BuildingInfo.class))).thenReturn(buildingInfo);

        Member member = Member.builder()
                .id(1L)
                .email(email)
                .password(password)
                .name("spring")
                .role(MemberRole.TENANT)
                .buildingInfos(Set.of(BuildingInfo.builder()
                        .dong("102동")
                        .ho("2212호")
                        .office(null)
                        .build()))
                .build();

        MemberRegisterRequestDto requestDto = new MemberRegisterRequestDto(email, password, "스프링", "103동", "2212호", MemberRole.TENANT);

        memberService.register(requestDto);

        when(memberRepository.findByEmailAndDeletedAtIsNull(email)).thenReturn(Optional.of(member));
        when(memberRepository.existsByEmailAndDeletedAtIsNull(email)).thenReturn(true);
        when(passwordEncoder.matches(password, member.getPassword())).thenReturn(true);
    }

    @Test
    @DisplayName("회원 탈퇴 메서드가 정상 작동한다.")
    void _willSuccess() {
        String email = "spring";
        String password = "spring";
        MemberWithdrawRequestDto memberRequest = new MemberWithdrawRequestDto(email, password);

        memberService.withdraw(memberRequest);

        verify(memberRepository).findByEmailAndDeletedAtIsNull(email);
        verify(memberRepository).save(any(Member.class));
    }

    @Test
    @DisplayName("다른 회원의 탈퇴를 시도한다.-사용자 불일치 오류 발생")
    void _willMissMatch() {
        String email = "spring";
        String password = "asdasdasd";
        MemberWithdrawRequestDto memberRequest = new MemberWithdrawRequestDto(email, password);

        Assertions.assertThrows(MemberMissMatchException.class, () -> memberService.withdraw(memberRequest));
    }

    @Test
    @DisplayName("회원 탈퇴하려는 계정이 존재하지 않는다.")
    void _willNotExist() {
        String email = "2222";
        String password = "2222";
        MemberWithdrawRequestDto memberRequest = new MemberWithdrawRequestDto(email, password);

        Assertions.assertThrows(MemberNotExistException.class, () -> memberService.withdraw(memberRequest));
    }
}

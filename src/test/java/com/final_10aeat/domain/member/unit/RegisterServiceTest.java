package com.final_10aeat.domain.member.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import com.final_10aeat.domain.member.dto.request.MemberLoginRequestDto;
import com.final_10aeat.domain.member.dto.request.MemberRegisterRequestDto;
import com.final_10aeat.domain.member.entity.BuildingInfo;
import com.final_10aeat.domain.member.entity.Member;
import com.final_10aeat.common.enumclass.MemberRole;
import com.final_10aeat.domain.member.exception.DisagreementException;
import com.final_10aeat.domain.member.exception.EmailDuplicatedException;
import com.final_10aeat.domain.member.repository.BuildingInfoRepository;
import com.final_10aeat.domain.member.repository.MemberRepository;
import com.final_10aeat.domain.member.service.MemberService;
import java.util.Set;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

public class RegisterServiceTest {

    @Mock
    private MemberRepository memberRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private BuildingInfoRepository buildingInfoRepository;
    @InjectMocks
    private MemberService memberService;

    private final String email = "test@test.com";
    private final String password = "password";
    private final String name = "spring";
    private final String dong = "102동";
    private final String ho = "2212호";
    private final MemberRole role = MemberRole.TENANT;


    private final MemberRegisterRequestDto request = new MemberRegisterRequestDto(email, password,
        name, dong, ho, role, true);
    private final BuildingInfo buildingInfo = BuildingInfo.builder()
        .dong(dong)
        .ho(ho)
        .office(null)
        .build();
    private final Member member = Member.builder()
        .id(1L)
        .email(email)
        .password(password)
        .name(name)
        .role(role)
        .buildingInfos(Set.of(BuildingInfo.builder()
            .dong(dong)
            .ho(ho)
            .office(null)
            .build()))
        .build();

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Nested
    @DisplayName("register()는")
    class Context_Register {

        @Test
        @DisplayName("회원가입에 성공한다.")
        void _willSuccess() {
            //given
            given(buildingInfoRepository.save(any(BuildingInfo.class))).willReturn(buildingInfo);
            given(passwordEncoder.matches(password, member.getPassword())).willReturn(true);
            given(memberRepository.save(any(Member.class))).willReturn(member);

            //when
            MemberLoginRequestDto loginRequest = memberService.register(request);

            //then
            assertEquals(new MemberLoginRequestDto(email, password), loginRequest);
        }

        @Test
        @DisplayName("이메일이 중복된 회원의 가입을 시도하여 실패한다.")
        void _willDuplicatedEmail() {
            //given
            given(buildingInfoRepository.save(any(BuildingInfo.class))).willReturn(buildingInfo);
            given(passwordEncoder.matches(password, member.getPassword())).willReturn(true);
            given(memberRepository.save(any(Member.class))).willReturn(member);
            given(memberRepository.existsByEmailAndDeletedAtIsNull(email)).willReturn(true);

            //then
            Assertions.assertThrows(EmailDuplicatedException.class,
                () -> memberService.register(request));
        }

        @Test
        @DisplayName("약관에 동의하지 않아 가입에 실패한다.")
        void _willDisagreeTerm() {
            //given
            given(buildingInfoRepository.save(any(BuildingInfo.class))).willReturn(buildingInfo);
            given(passwordEncoder.matches(password, member.getPassword())).willReturn(true);
            given(memberRepository.save(any(Member.class))).willReturn(member);
            given(memberRepository.existsByEmailAndDeletedAtIsNull(email)).willReturn(false);
            MemberRegisterRequestDto disagreeRequest = new MemberRegisterRequestDto(email, password,
                name, dong, ho, role, false);

            //then
            Assertions.assertThrows(DisagreementException.class,
                () -> memberService.register(disagreeRequest));
        }
    }
}

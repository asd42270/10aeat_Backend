package com.final_10aeat.domain.member.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;

import com.final_10aeat.domain.member.dto.request.MemberLoginRequestDto;
import com.final_10aeat.domain.member.entity.Member;
import com.final_10aeat.domain.member.entity.MemberRole;
import com.final_10aeat.domain.member.exception.MemberNotExistException;
import com.final_10aeat.domain.member.repository.MemberRepository;
import com.final_10aeat.domain.member.service.MemberService;
import com.final_10aeat.global.security.jwt.JwtTokenGenerator;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

class MemberServiceTest {

    @Mock
    private MemberRepository memberRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtTokenGenerator jwtTokenGenerator;
    @InjectMocks
    private MemberService memberService;

    private final String email = "test@test.com";
    private final String password = "password";
    private final MemberLoginRequestDto loginRequest = new MemberLoginRequestDto(email, password);
    private final Member member = Member.builder()
        .email(email)
        .password(password)
        .role(MemberRole.OWNER)
        .build();

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Nested
    @DisplayName("login()는")
    class Context_LoginCode {

        @Test
        @DisplayName("로그인에 성공한다.")
        void _willSuccess() {
            //given
            String accessToken = "token";

            given(memberRepository.findByEmail(email))
                .willReturn(Optional.of(member));
            given(passwordEncoder.matches(password, member.getPassword()))
                .willReturn(Boolean.TRUE);
            given(jwtTokenGenerator.createJwtToken(email, MemberRole.OWNER))
                .willReturn(accessToken);

            //when
            String response = memberService.login(loginRequest);

            //then
            assertEquals(accessToken, response);
        }

        @Test
        @DisplayName("존재하지 않는 이메일을 요청하여 실패한다.")
        void NotFound_willFail() {
            //given
            given(memberRepository.findByEmail(email))
                .willReturn(Optional.empty());

            // when & then
            assertThrows(MemberNotExistException.class,
                () -> memberService.login(loginRequest)
            );
        }

        @Test
        @DisplayName("비밀번호가 달라 요청에 실패한다.")
        void NotMatcher_willFail() {
            //given
            given(memberRepository.findByEmail(email))
                .willReturn(Optional.of(member));
            given(passwordEncoder.matches(password, member.getPassword()))
                .willReturn(Boolean.FALSE);

            // when & then
            assertThrows(MemberNotExistException.class,
                () -> memberService.login(loginRequest));
        }
    }
}

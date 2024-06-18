package com.final_10aeat.domain.member.unit;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.final_10aeat.common.enumclass.MemberRole;
import com.final_10aeat.domain.member.dto.request.CreateMemberRequestDto;
import com.final_10aeat.domain.member.entity.BuildingInfo;
import com.final_10aeat.domain.member.entity.Member;
import com.final_10aeat.domain.member.exception.DisagreementException;
import com.final_10aeat.domain.member.exception.EmailDuplicateException;
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
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.crypto.password.PasswordEncoder;

public class RegisterServiceTest {

    @InjectMocks
    private MemberService memberService;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private BuildingInfoRepository buildingInfoRepository;

    @Mock
    private RedisTemplate<String, String> redisTemplate;

    @Mock
    private ValueOperations<String, String> valueOperations;

    private final CreateMemberRequestDto request = new CreateMemberRequestDto(
        "test@test.com", "password", "spring", "102동", "2212호", MemberRole.TENANT, true);

    private final BuildingInfo buildingInfo = BuildingInfo.builder()
        .dong("102동")
        .ho("2212호")
        .office(null)
        .build();

    private final Member member = Member.builder()
        .id(1L)
        .email("test@test.com")
        .password("encodedPassword")
        .name("spring")
        .role(MemberRole.TENANT)
        .buildingInfos(Set.of(buildingInfo))
        .build();

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        when(valueOperations.get(anyString())).thenReturn(
            "{\"officeId\":1,\"dong\":\"102동\",\"ho\":\"2212호\"}");
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(buildingInfoRepository.save(any(BuildingInfo.class))).thenReturn(buildingInfo);
        when(memberRepository.save(any(Member.class))).thenReturn(member);
    }

    @Nested
    @DisplayName("register()는")
    class Context_Register {

        @Test
        @DisplayName("회원가입에 성공한다.")
        void _willSuccess() {
            // Given
            given(memberRepository.existsByEmailAndDeletedAtIsNull(request.email())).willReturn(
                false);

            // When
            memberService.register(request);

            // Then
            verify(memberRepository).save(any(Member.class));
            verify(redisTemplate.opsForValue(), times(2)).get(
                anyString());
        }

        @Test
        @DisplayName("이메일이 중복되어 회원 가입에 실패한다.")
        void _willDuplicatedEmail() {
            // Given
            given(memberRepository.existsByEmailAndDeletedAtIsNull(request.email())).willReturn(
                true);

            // When & Then
            Assertions.assertThrows(EmailDuplicateException.class, () -> {
                memberService.register(request);
            });
        }

        @Test
        @DisplayName("약관에 동의하지 않아 가입에 실패한다.")
        void _willDisagreeTerm() {
            // Given
            CreateMemberRequestDto disagreeRequest = new CreateMemberRequestDto(
                "test@test.com", "password", "spring", "102동", "2212호", MemberRole.TENANT, false);

            // When & Then
            Assertions.assertThrows(DisagreementException.class, () -> {
                memberService.register(disagreeRequest);
            });
        }
    }
}

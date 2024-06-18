package com.final_10aeat.domain.member.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.final_10aeat.domain.manager.dto.response.VerifyEmailResponseDto;
import com.final_10aeat.common.enumclass.MemberRole;
import com.final_10aeat.domain.manager.exception.ExpiredVerificationCodeException;
import com.final_10aeat.domain.manager.exception.InvalidVerificationCodeException;
import com.final_10aeat.domain.manager.service.LocalEmailService;
import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.concurrent.TimeUnit;

public class LocalEmailServiceTest {

    @Mock
    private RedisTemplate<String, String> redisTemplate;

    @Mock
    private ValueOperations<String, String> valueOperations;

    @InjectMocks
    private LocalEmailService localEmailService;

    private Gson gson = new Gson();

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
    }

    @Test
    @DisplayName("인증 코드 전송을 성공한다.")
    void _willSuccess() {
        String email = "test@example.com";
        MemberRole role = MemberRole.TENANT;
        String dong = "102";
        String ho = "101";
        Long officeId = 1L;

        String authCode = localEmailService.sendVerificationEmail(email, role, dong, ho, officeId);

        verify(valueOperations).set(eq(email + ":code"), eq(authCode), eq(1440L),
            eq(TimeUnit.MINUTES));
        verify(valueOperations).set(eq(email + ":info"), anyString(), eq(1440L),
            eq(TimeUnit.MINUTES));
    }

    @Nested
    @DisplayName("verifyEmailCode()는")
    class Context_VerifyEmailCode {

        @Test
        @DisplayName("검증을 성공한다.")
        void _willSuccess() {
            String email = "test@example.com";
            String code = "6pidjym";
            VerifyEmailResponseDto expectedResponse = new VerifyEmailResponseDto(email,
                "TENANT", "102", "101");
            String userInfoJson = gson.toJson(expectedResponse);

            when(valueOperations.get(email + ":code")).thenReturn(code);
            when(valueOperations.get(email + ":info")).thenReturn(userInfoJson);

            VerifyEmailResponseDto response = localEmailService.verifyEmailCode(email, code);
            assertEquals(expectedResponse, response);
        }

        @Test
        @DisplayName("일치하지 않는 인증 코드로 검증에 실패한다.")
        void BadRequest_willFail() {
            String email = "test@example.com";
            String code = "6pidjym";
            String wrongCode = "wrongcode";
            VerifyEmailResponseDto expectedResponse = new VerifyEmailResponseDto(email,
                "TENANT", "102", "101");
            String userInfoJson = gson.toJson(expectedResponse);

            when(valueOperations.get(email + ":code")).thenReturn(wrongCode);
            when(valueOperations.get(email + ":info")).thenReturn(userInfoJson);

            assertThrows(InvalidVerificationCodeException.class, () -> {
                localEmailService.verifyEmailCode(email, code);
            });
        }

        @Test
        @DisplayName("인증 코드가 만료되어 검증에 실패한다.")
        void Gone_willFailure() {
            String email = "test@example.com";
            String code = "6pidjym";

            when(valueOperations.get(email + ":code")).thenReturn(null);
            when(valueOperations.get(email + ":info")).thenReturn(null);

            assertThrows(ExpiredVerificationCodeException.class,
                () -> localEmailService.verifyEmailCode(email, code));
        }
    }
}

package com.final_10aeat.domain.manager.docs;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.final_10aeat.common.enumclass.MemberRole;
import com.final_10aeat.common.util.manager.WithManager;
import com.final_10aeat.docs.RestDocsSupport;
import com.final_10aeat.domain.manager.controller.EmailController;
import com.final_10aeat.domain.manager.dto.request.EmailRequestDto;
import com.final_10aeat.domain.manager.dto.request.VerifyEmailRequestDto;
import com.final_10aeat.domain.manager.dto.response.VerifyEmailResponseDto;
import com.final_10aeat.domain.manager.service.EmailUseCase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

public class EmailControllerDocsTest extends RestDocsSupport {

    private EmailUseCase emailUseCase;
    private ObjectMapper objectMapper;

    @Override
    public Object initController() {
        emailUseCase = mock(EmailUseCase.class);
        objectMapper = new ObjectMapper();

        return new EmailController(emailUseCase);
    }

    @DisplayName("이메일 전송 API 문서화")
    @Test
    @WithManager
    void testSendVerificationEmail() throws Exception {
        // given
        EmailRequestDto emailRequest = new EmailRequestDto(
            "test@example.com", MemberRole.TENANT, "102", "101");

        when(emailUseCase.sendVerificationEmail(anyString(), any(MemberRole.class), anyString(),
            anyString(), any(Long.class)))
            .thenReturn("Verification email sent");

        // when & then
        mockMvc.perform(post("/members/email")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(emailRequest)
                )
            )
            .andExpect(status().isOk())
            .andDo(document("email-send-verification",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestFields(
                    fieldWithPath("email").description("이메일"),
                    fieldWithPath("role").description("회원 권한 (OWNER, TENANT)"),
                    fieldWithPath("dong").description("동 정보"),
                    fieldWithPath("ho").description("호 정보")
                ),
                responseFields(
                    fieldWithPath("code").description("응답 상태 코드")
                )
            ));
    }

    @DisplayName("이메일 인증 코드 검증 API 문서화")
    @Test
    void testVerifyEmailCode() throws Exception {
        // given
        VerifyEmailRequestDto verificationRequest = new VerifyEmailRequestDto(
            "test@example.com", "6pidjym");

        VerifyEmailResponseDto responseDto = new VerifyEmailResponseDto(
            "test@example.com", "TENANT", "102", "101");

        when(emailUseCase.verifyEmailCode(anyString(), anyString()))
            .thenReturn(responseDto);

        // when & then
        mockMvc.perform(post("/members/email/verification")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(verificationRequest)))
            .andExpect(status().isOk())
            .andDo(document("email-verify-code",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestFields(
                    fieldWithPath("email").description("이메일"),
                    fieldWithPath("code").description("인증 코드")
                ),
                responseFields(
                    fieldWithPath("code").description("응답 상태 코드"),
                    fieldWithPath("data.email").description("이메일"),
                    fieldWithPath("data.role").description("회원 권한 (OWNER, TENANT)"),
                    fieldWithPath("data.dong").description("동 정보"),
                    fieldWithPath("data.ho").description("호 정보")
                )
            ));
    }
}

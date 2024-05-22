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
import com.final_10aeat.docs.RestDocsSupport;
import com.final_10aeat.domain.manager.controller.EmailController;
import com.final_10aeat.domain.manager.dto.request.EmailRequestDto;
import com.final_10aeat.domain.manager.dto.request.EmailVerificationRequestDto;
import com.final_10aeat.domain.manager.dto.response.EmailVerificationResponseDto;
import com.final_10aeat.domain.manager.entity.Manager;
import com.final_10aeat.domain.manager.service.EmailUseCase;
import com.final_10aeat.domain.manager.service.ManagerService;
import com.final_10aeat.domain.office.entity.Office;
import com.final_10aeat.global.security.principal.ManagerPrincipal;
import com.final_10aeat.global.util.AuthoritiesUtil;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;

public class EmailControllerDocsTest extends RestDocsSupport {

    private EmailUseCase emailUseCase;
    private ManagerService managerService;
    private ObjectMapper objectMapper;

    @Override
    public Object initController() {
        emailUseCase = mock(EmailUseCase.class);
        managerService = mock(ManagerService.class);
        objectMapper = new ObjectMapper();

        return new EmailController(emailUseCase);
    }

   /* @DisplayName("이메일 전송 API 문서화")
    @Test
    void testSendVerificationEmail() throws Exception {
        // given
        EmailRequestDto emailRequest = new EmailRequestDto(
            "test@example.com", MemberRole.TENANT, "102", "101");

        Office office = Office.builder()
            .id(1L)
            .officeName("Office Name")
            .address("123 Main St")
            .mapX(123.45)
            .mapY(678.90)
            .build();

        office.getId(), office.getOfficeName(), office.getAddress(), office.getMapX(), office.getMapY());

        Manager manager = Manager.builder()
            .email("manager@example.com")
            .password("password")
            .name("John Doe")
            .phoneNumber("123-456-7890")
            .lunchBreakStart(LocalDateTime.of(2024, 5, 22, 12, 0))
            .lunchBreakEnd(LocalDateTime.of(2024, 5, 22, 13, 0))
            .managerOffice("Manager Office")
            .affiliation("Affiliation")
            .role(MemberRole.MANAGER)
            .office(office)
            .build();

        manager.getEmail(), manager.getPassword(), manager.getName(), manager.getPhoneNumber(), manager.getLunchBreakStart(), manager.getLunchBreakEnd(), manager.getManagerOffice(), manager.getAffiliation(), manager.getRole(), manager.getOffice())
        ;
        ManagerPrincipal managerPrincipal = new ManagerPrincipal(manager);

        when(emailUseCase.sendVerificationEmail(anyString(), any(MemberRole.class), anyString(),
            anyString(), any(Long.class)))
            .thenReturn("Verification email sent");

        SecurityContextHolder.getContext().setAuthentication(
            new UsernamePasswordAuthenticationToken(managerPrincipal, null,
                AuthoritiesUtil.MANAGER_AUTHORITIES)
        );

        // when & then
        mockMvc.perform(post("/members/email")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(emailRequest))
                .with(SecurityMockMvcRequestPostProcessors.authentication(
                    new UsernamePasswordAuthenticationToken(managerPrincipal, null,
                        AuthoritiesUtil.MANAGER_AUTHORITIES)
                )))
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
                    fieldWithPath("code").description("응답 상태 코드"),
                    fieldWithPath("data").description("응답 데이터")
                )
            ));
    }*/

    @DisplayName("이메일 인증 코드 검증 API 문서화")
    @Test
    void testVerifyEmailCode() throws Exception {
        // given
        EmailVerificationRequestDto verificationRequest = new EmailVerificationRequestDto(
            "test@example.com", "6pidjym");

        EmailVerificationResponseDto responseDto = new EmailVerificationResponseDto(
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

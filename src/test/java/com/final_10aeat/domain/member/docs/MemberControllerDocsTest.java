package com.final_10aeat.domain.member.docs;

import static org.mockito.Mockito.when;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.final_10aeat.docs.RestDocsSupport;
import com.final_10aeat.domain.member.controller.MemberController;
import com.final_10aeat.domain.member.dto.request.MemberLoginRequestDto;
import com.final_10aeat.domain.member.dto.request.MemberRegisterRequestDto;
import com.final_10aeat.domain.member.dto.request.MemberWithdrawRequestDto;
import com.final_10aeat.common.enumclass.MemberRole;
import com.final_10aeat.domain.member.service.MemberService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

public class MemberControllerDocsTest extends RestDocsSupport {

    private MemberService memberService;
    private ObjectMapper objectMapper;

    @Override
    public Object initController() {
        memberService = Mockito.mock(MemberService.class);
        objectMapper = new ObjectMapper();
        return new MemberController(memberService);
    }

    @BeforeEach
    public void setUp(RestDocumentationContextProvider restDocumentation) {
        mockMvc = MockMvcBuilders
            .standaloneSetup(initController())
            .apply(documentationConfiguration(restDocumentation))
            .build();
    }

    @DisplayName("로그인 API 문서화")
    @Test
    void testLogin() throws Exception {
        //given
        MemberLoginRequestDto loginRequest = new MemberLoginRequestDto(
            "test@example.com", "password"
        );

        // when
        when(memberService.login(loginRequest))
            .thenReturn("token");

        // then
        mockMvc.perform(RestDocumentationRequestBuilders.post("/members/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
            .andExpect(status().isOk())
            .andDo(document("member-login",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestFields(
                    fieldWithPath("email").description("로그인 할 계정의 이메일"),
                    fieldWithPath("password").description("로그인 할 회원의 비밀번호")
                ),
                responseHeaders(
                    headerWithName("accessToken").description("로그인 정보가 포함된 토큰")
                )
                ,
                responseFields(
                    fieldWithPath("code").description("응답 상태 코드")
                )
            ));
    }

    @DisplayName("회원가입 API 문서화")
    @Test
    void testRegister() throws Exception {
        //given
        MemberRegisterRequestDto registerRequest = new MemberRegisterRequestDto(
                "test@example.com", "password", "test",
                "102동", "2212호", MemberRole.TENANT, true
        );

        MemberLoginRequestDto loginRequest = new MemberLoginRequestDto(
                "test@example.com", "password"
        );

        // when
        when(memberService.register(registerRequest)).thenReturn(loginRequest);
        when(memberService.login(loginRequest)).thenReturn("token");

        // then
        mockMvc.perform(RestDocumentationRequestBuilders.post("/members")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isOk())
                .andDo(document("member-register",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("email").description("회원의 이메일"),
                                fieldWithPath("password").description("회원의 비밀번호"),
                                fieldWithPath("name").description("회원의 이름"),
                                fieldWithPath("dong").description("회원이 소유한 동-호수의 동"),
                                fieldWithPath("ho").description("회원이 소유한 동-호수의 호수"),
                                fieldWithPath("memberRole").description("회원의 역할"),
                                fieldWithPath("isTermAgreed").description("약관 동의 여부")
                        ),
                        responseHeaders(
                                headerWithName("accessToken").description("로그인 정보가 포함된 토큰")
                        )
                        ,
                        responseFields(
                                fieldWithPath("code").description("응답 상태 코드")
                        )
                ));
    }

    @DisplayName("회원탈퇴 API 문서화")
    @Test
    void testWithdraw() throws Exception {
        //given
        MemberWithdrawRequestDto withdrawRequest = new MemberWithdrawRequestDto(
                "test@example.com", "password");

        // when
        memberService.withdraw(withdrawRequest);

        // then
        mockMvc.perform(RestDocumentationRequestBuilders.delete("/members")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(withdrawRequest)))
                .andExpect(status().isOk())
                .andDo(document("member-withdraw",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("email").description("회원의 이메일"),
                                fieldWithPath("password").description("회원의 비밀번호")
                        ),
                        responseFields(
                                fieldWithPath("code").description("응답 상태 코드")
                        )
                ));
    }
}

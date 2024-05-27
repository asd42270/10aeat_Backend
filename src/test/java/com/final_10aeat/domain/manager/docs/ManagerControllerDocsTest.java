package com.final_10aeat.domain.manager.docs;

import static org.mockito.Mockito.when;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.final_10aeat.common.enumclass.MemberRole;
import com.final_10aeat.docs.RestDocsSupport;
import com.final_10aeat.domain.manager.controller.ManagerController;
import com.final_10aeat.domain.manager.dto.request.CreateManagerRequestDto;
import com.final_10aeat.domain.manager.entity.Manager;
import com.final_10aeat.domain.manager.repository.ManagerRepository;
import com.final_10aeat.domain.manager.service.ManagerService;
import com.final_10aeat.domain.member.dto.request.MemberLoginRequestDto;
import com.final_10aeat.domain.office.entity.Office;
import com.final_10aeat.domain.office.repository.OfficeRepository;
import com.final_10aeat.global.security.jwt.JwtTokenGenerator;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

public class ManagerControllerDocsTest extends RestDocsSupport {

    private ManagerService managerService;
    private ManagerRepository managerRepository;
    private OfficeRepository officeRepository;
    private PasswordEncoder passwordEncoder;
    private JwtTokenGenerator jwtTokenGenerator;
    private ObjectMapper objectMapper;

    @Override
    public Object initController() {
        managerRepository = Mockito.mock(ManagerRepository.class);
        officeRepository = Mockito.mock(OfficeRepository.class);
        passwordEncoder = Mockito.mock(PasswordEncoder.class);
        jwtTokenGenerator = Mockito.mock(JwtTokenGenerator.class);
        managerService = new ManagerService(managerRepository, officeRepository, passwordEncoder,
            jwtTokenGenerator);
        objectMapper = new ObjectMapper();
        return new ManagerController(managerService);
    }

    @BeforeEach
    public void setUp(RestDocumentationContextProvider restDocumentation) {
        mockMvc = MockMvcBuilders
            .standaloneSetup(initController())
            .apply(documentationConfiguration(restDocumentation))
            .build();

        Office office = Office.builder()
            .id(1L)
            .officeName("미왕 빌딩")
            .address("123 Gangnam St.")
            .mapX(35.6895)
            .mapY(139.6917)
            .build();
        when(officeRepository.findById(1L)).thenReturn(Optional.of(office));

        Manager manager = Manager.builder()
            .id(1L)
            .email("manager@example.com")
            .password("managerPassword")
            .name("김관리")
            .phoneNumber("010-1234-5678")
            .lunchBreakStart(null)
            .lunchBreakEnd(null)
            .managerOffice("중앙 현관 1층 관리자 사무실")
            .affiliation("김씨 관리 협회")
            .office(office)
            .role(MemberRole.MANAGER)
            .build();
        when(managerRepository.findByEmail("manager@example.com")).thenReturn(Optional.of(manager));
        when(managerRepository.findById(1L)).thenReturn(
            Optional.of(manager));
        when(passwordEncoder.matches("managerPassword", "managerPassword")).thenReturn(true);
    }

    @DisplayName("관리자 등록 API 문서화")
    @Test
    void testRegisterManager() throws Exception {
        // Given
        CreateManagerRequestDto registerRequest = new CreateManagerRequestDto(
            "manager@naver.com", "managerPassword", "김관리", "010-1234-5678", null, null,
            "중앙 현관 1층 관리자 사무실", "김씨 관리 협회", 1L
        );

        when(managerService.register(registerRequest)).thenReturn(null);

        // When & Then
        mockMvc.perform(RestDocumentationRequestBuilders.post("/managers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerRequest)))
            .andExpect(status().isOk())
            .andDo(document("manager-register",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestFields(
                    fieldWithPath("email").description("이메일"),
                    fieldWithPath("password").description("비밀번호"),
                    fieldWithPath("name").description("이름"),
                    fieldWithPath("phoneNumber").description("전화번호"),
                    fieldWithPath("lunchBreakStart").optional().description("점심시간 시작"),
                    fieldWithPath("lunchBreakEnd").optional().description("점심시간 끝"),
                    fieldWithPath("managerOffice").description("관리 사무소 위치"),
                    fieldWithPath("affiliation").description("소속 정보"),
                    fieldWithPath("officeId").description("관리하고 있는 건물 ID")
                ),
                responseFields(
                    fieldWithPath("code").description("응답 상태 코드")
                )
            ));
    }

    @DisplayName("관리자 로그인 API 문서화")
    @Test
    void testLoginManager() throws Exception {
        // Given
        MemberLoginRequestDto loginRequest = new MemberLoginRequestDto(
            "manager@example.com", "managerPassword"
        );

        // When
        String token = "mockToken";
        when(managerService.login(loginRequest)).thenReturn(token);

        // Then
        mockMvc.perform(RestDocumentationRequestBuilders.post("/managers/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
            .andExpect(status().isOk())
            .andDo(document("manager-login",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestFields(
                    fieldWithPath("email").description("이메일"),
                    fieldWithPath("password").description("비밀번호")
                ),
                responseHeaders(
                    headerWithName("accessToken").description("발급된 액세스 토큰")
                ),
                responseFields(
                    fieldWithPath("code").description("응답 상태 코드")
                )
            ));
    }

    @DisplayName("매니저 정보 조회 API 문서화")
    @Test
    void testGetManagerProfile() throws Exception {
        Long managerId = 1L;

        mockMvc.perform(get("/managers/profile/{managerId}", managerId))
            .andExpect(status().isOk())
            .andDo(document("manager-profile",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                pathParameters(
                    parameterWithName("managerId").description("관리자 ID")
                ),
                responseFields(
                    fieldWithPath("code").description("응답 상태 코드"),
                    fieldWithPath("data.email").description("이메일"),
                    fieldWithPath("data.name").description("이름"),
                    fieldWithPath("data.phoneNumber").description("전화번호"),
                    fieldWithPath("data.lunchBreakStart").optional().description("점심시간 시작"),
                    fieldWithPath("data.lunchBreakEnd").optional().description("점심시간 끝"),
                    fieldWithPath("data.managerOffice").description("관리 사무소 위치"),
                    fieldWithPath("data.affiliation").description("소속 정보")
                )
            ));
    }
}

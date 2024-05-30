package com.final_10aeat.domain.manageArticle.docs;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
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
import com.final_10aeat.common.enumclass.ManagePeriod;
import com.final_10aeat.common.util.manager.WithManager;
import com.final_10aeat.docs.RestDocsSupport;
import com.final_10aeat.domain.manageArticle.controller.ManagerManageArticleController;
import com.final_10aeat.domain.manageArticle.dto.request.CreateManageArticleRequestDto;
import com.final_10aeat.domain.manageArticle.dto.request.UpdateManageArticleRequestDto;
import com.final_10aeat.domain.manageArticle.service.ManagerManageArticleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

public class ManagerManageArticleControllerDocsTest extends RestDocsSupport {

    private ManagerManageArticleService managerManageArticleService;
    private ObjectMapper objectMapper;

    @Override
    public Object initController() {
        managerManageArticleService = Mockito.mock(ManagerManageArticleService.class);
        objectMapper = new ObjectMapper();
        return new ManagerManageArticleController(managerManageArticleService);
    }

    @BeforeEach
    public void setUp(RestDocumentationContextProvider restDocumentation) {
        mockMvc = MockMvcBuilders
            .standaloneSetup(initController())
            .apply(documentationConfiguration(restDocumentation))
            .build();
    }

    @DisplayName("Manager Article 생성 API 문서화")
    @Test
    @WithManager
    void testCreate() throws Exception {
        // given
        CreateManageArticleRequestDto request = new CreateManageArticleRequestDto(
            ManagePeriod.WEEK,
            1,
            "유지관리 게시글 제목",
            "법적 근거",
            "관리 대상",
            "담당자/수리업체",
            "비고"
        );

        // when & then
        mockMvc.perform(RestDocumentationRequestBuilders.post("/managers/manage/articles")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andDo(document("create-manage-article",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestFields(
                    fieldWithPath("period").description("주기")
                        .description(
                            "WEEK, MONTH, HALF_YEAR, YEAR, TWO_YEAR, THREE_YEA FOUR_YEAR, FIVE_YEAR, ETC"),
                    fieldWithPath("periodCount").description("횟수"),
                    fieldWithPath("title").description("유지관리 제목"),
                    fieldWithPath("legalBasis").description("법적 근거").optional(),
                    fieldWithPath("target").description("관리 대상").optional(),
                    fieldWithPath("responsibility").description("담당 업체").optional(),
                    fieldWithPath("note").description("비고").optional()
                ),
                responseFields(
                    fieldWithPath("code").description("응답 상태 코드")
                )
            ));
    }

    @DisplayName("Manager Article 수정 API 문서화")
    @Test
    @WithManager
    void testUpdate() throws Exception {
        // given
        UpdateManageArticleRequestDto request = new UpdateManageArticleRequestDto(
            ManagePeriod.WEEK,
            1,
            "유지관리 게시글 제목",
            "법적 근거",
            "관리 대상",
            "담당자/수리업체",
            "비고"
        );

        // when & then
        mockMvc.perform(
                RestDocumentationRequestBuilders.patch("/managers/manage/articles/{manageArticleId}", 1)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andDo(document("update-manage-article",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestFields(
                    fieldWithPath("period").description("주기")
                        .description(
                            "WEEK, MONTH, HALF_YEAR, YEAR, TWO_YEAR, THREE_YEA FOUR_YEAR, FIVE_YEAR, ETC")
                        .optional(),
                    fieldWithPath("periodCount").description("횟수").optional(),
                    fieldWithPath("title").description("유지관리 제목").optional(),
                    fieldWithPath("legalBasis").description("법적 근거").optional(),
                    fieldWithPath("target").description("관리 대상").optional(),
                    fieldWithPath("responsibility").description("담당 업체").optional(),
                    fieldWithPath("note").description("비고").optional()
                ),
                pathParameters(
                    parameterWithName("manageArticleId").description("유지관리 게시글 ID")
                ),
                responseFields(
                    fieldWithPath("code").description("응답 상태 코드")
                )
            ));
    }
}

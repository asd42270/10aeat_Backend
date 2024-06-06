package com.final_10aeat.domain.issue.docs;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
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
import com.final_10aeat.common.util.member.WithMember;
import com.final_10aeat.docs.RestDocsSupport;
import com.final_10aeat.domain.articleIssue.controller.ArticleIssueCheckController;
import com.final_10aeat.domain.articleIssue.dto.request.ArticleIssueCheckRequestDto;
import com.final_10aeat.domain.articleIssue.dto.response.ArticleIssueCheckResponseDto;
import com.final_10aeat.domain.articleIssue.service.ArticleIssueCheckService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

public class ArticleIssueCheckControllerDocsTest extends RestDocsSupport {

    private final ArticleIssueCheckService articleIssueCheckService = Mockito.mock(
        ArticleIssueCheckService.class);

    @Override
    public Object initController() {
        objectMapper = new ObjectMapper();
        return new ArticleIssueCheckController(articleIssueCheckService);
    }

    @BeforeEach
    public void setUp(RestDocumentationContextProvider restDocumentation) {
        mockMvc = MockMvcBuilders
            .standaloneSetup(initController())
            .apply(documentationConfiguration(restDocumentation))
            .build();
    }

    @DisplayName("이슈 확인 API 문서화")
    @Test
    @WithMember
    void testIssueCheck() throws Exception {
        //given
        ArticleIssueCheckRequestDto requestDto = new ArticleIssueCheckRequestDto(true);

        // when & then
        mockMvc.perform(RestDocumentationRequestBuilders.post("/articles/issue/check/{issueId}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)))
            .andExpect(status().isOk())
            .andDo(document("issue-check",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                pathParameters(
                    parameterWithName("issueId").description("이슈 ID")
                ),
                requestFields(
                    fieldWithPath("check").description("이슈 확인 여부")
                ),
                responseFields(
                    fieldWithPath("code").description("응답 상태 코드")
                )
            ));
    }

    @DisplayName("이슈 상세조회 API 문서화")
    @Test
    void testIssueDetail() throws Exception {
        // given
        ArticleIssueCheckResponseDto responseDto = new ArticleIssueCheckResponseDto("이슈 제목",
            "이슈 내용");

        // when
        when(articleIssueCheckService.getIssueDetail(any(Long.class))).thenReturn(responseDto);

        // then
        mockMvc.perform(RestDocumentationRequestBuilders.get("/articles/issue/detail/{issueId}", 1))
            .andExpect(status().isOk())
            .andDo(document("issue-detail",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                pathParameters(
                    parameterWithName("issueId").description("이슈 ID")
                ),
                responseFields(
                    fieldWithPath("code").description("응답 상태 코드"),
                    fieldWithPath("data.title").description("이슈 제목"),
                    fieldWithPath("data.content").description("이슈 내용")
                )
            ));
    }
}

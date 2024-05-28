package com.final_10aeat.domain.issue.docs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.final_10aeat.common.util.member.WithMember;
import com.final_10aeat.docs.RestDocsSupport;
import com.final_10aeat.domain.articleIssue.controller.ArticleIssueCheckController;
import com.final_10aeat.domain.articleIssue.dto.ArticleIssueCheckRequestDto;
import com.final_10aeat.domain.articleIssue.service.ArticleIssueCheckService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ArticleIssueCheckControllerDocsTest extends RestDocsSupport {

    @Override
    public Object initController() {
        ArticleIssueCheckService articleIssueCheckService = Mockito.mock(ArticleIssueCheckService.class);
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

        // when&then
        mockMvc.perform(RestDocumentationRequestBuilders.post("/issues/check/{issue_id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andDo(document("issue-check",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("issue_id").description("이슈 id")
                        ),
                        requestFields(
                                fieldWithPath("check").description("이슈 확인 여부")
                        ),
                        responseFields(
                                fieldWithPath("code").description("응답 상태 코드")
                        )
                ));
    }

}

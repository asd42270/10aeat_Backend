package com.final_10aeat.domain.issue.docs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.final_10aeat.common.service.AuthenticationService;
import com.final_10aeat.common.util.manager.WithManager;
import com.final_10aeat.docs.RestDocsSupport;
import com.final_10aeat.domain.articleIssue.controller.ArticleIssueController;
import com.final_10aeat.domain.articleIssue.dto.request.ArticleIssuePublishRequestDto;
import com.final_10aeat.domain.articleIssue.service.ArticleIssueService;
import com.final_10aeat.domain.manager.entity.Manager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ArticleIssueControllerDocsTest extends RestDocsSupport {

    private ArticleIssueService articleIssueService;
    private AuthenticationService authenticationService;
    private ObjectMapper objectMapper;

    @Override
    public Object initController() {
        articleIssueService = Mockito.mock(ArticleIssueService.class);
        authenticationService = Mockito.mock(AuthenticationService.class);

        objectMapper = new ObjectMapper();
        return new ArticleIssueController(articleIssueService, authenticationService);
    }

    @BeforeEach
    public void setUp(RestDocumentationContextProvider restDocumentation) {
        mockMvc = MockMvcBuilders
                .standaloneSetup(initController())
                .apply(documentationConfiguration(restDocumentation))
                .build();
    }

    @DisplayName("유지보수 이슈 발행 API 문서화")
    @Test
    @WithManager
    void testRepairIssuePublic() throws Exception {
        //given
        ArticleIssuePublishRequestDto issueRequest = new ArticleIssuePublishRequestDto(
                "title", "content");

        doNothing().when(articleIssueService).manageIssuePublish(eq(issueRequest), any(Long.class), any(Manager.class));

        // when&then
        mockMvc.perform(RestDocumentationRequestBuilders.post("/managers/articles/manage/issue/{manage_article_id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(issueRequest)))
                .andExpect(status().isOk())
                .andDo(document("manage-issue-publish",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("manage_article_id").description("유지관리 게시글 id")
                        ),
                        requestFields(
                                fieldWithPath("title").description("발행할 이슈의 제목"),
                                fieldWithPath("content").description("발행할 이슈의 내용")
                        ),
                        responseFields(
                                fieldWithPath("code").description("응답 상태 코드")
                        )
                ));
    }

    @DisplayName("유지관리 이슈 발행 API 문서화")
    @Test
    @WithManager
    void testManageIssuePublic() throws Exception {
        //given
        ArticleIssuePublishRequestDto issueRequest = new ArticleIssuePublishRequestDto(
                "title", "content");

        doNothing().when(articleIssueService).repairIssuePublish(eq(issueRequest), any(Long.class), any(Manager.class));

        // when&then
        mockMvc.perform(RestDocumentationRequestBuilders.post("/managers/articles/repair/issue/{repair_article_id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(issueRequest)))
                .andExpect(status().isOk())
                .andDo(document("repair-issue-publish",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("repair_article_id").description("유지보수 게시글 id")
                        ),
                        requestFields(
                                fieldWithPath("title").description("발행할 이슈의 제목"),
                                fieldWithPath("content").description("발행할 이슈의 내용")
                        ),
                        responseFields(
                                fieldWithPath("code").description("응답 상태 코드")
                        )
                ));
    }
}

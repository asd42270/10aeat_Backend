package com.final_10aeat.domain.issue.docs;

import static java.util.Arrays.asList;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.subsectionWithPath;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.final_10aeat.common.dto.UserIdAndRole;
import com.final_10aeat.common.service.AuthenticationService;
import com.final_10aeat.common.util.manager.WithManager;
import com.final_10aeat.docs.RestDocsSupport;
import com.final_10aeat.domain.articleIssue.controller.ArticleIssueController;
import com.final_10aeat.domain.articleIssue.dto.request.ArticleIssuePublishRequestDto;
import com.final_10aeat.domain.articleIssue.dto.request.IssueUpdateRequestDto;
import com.final_10aeat.domain.articleIssue.dto.response.IssueHistoryResponseDto;
import com.final_10aeat.domain.articleIssue.service.ArticleIssueService;
import com.final_10aeat.domain.manager.entity.Manager;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

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

        doNothing().when(articleIssueService)
            .manageIssuePublish(eq(issueRequest), any(Long.class), any(Manager.class));

        // when&then
        mockMvc.perform(RestDocumentationRequestBuilders.post(
                    "/managers/articles/manage/issue/{manage_article_id}", 1)
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

        doNothing().when(articleIssueService)
            .repairIssuePublish(eq(issueRequest), any(Long.class), any(Manager.class));

        // when&then
        mockMvc.perform(RestDocumentationRequestBuilders.post(
                    "/managers/articles/repair/issue/{repair_article_id}", 1)
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

    @DisplayName(" 이슈 수정 API 문서화")
    @Test
    @WithManager
    void updateIssue() throws Exception {

        // given
        IssueUpdateRequestDto request = IssueUpdateRequestDto.builder()
            .title("이슈 수정")
            .content("수정을 수정")
            .build();

        doNothing().when(articleIssueService)
            .updateIssue(eq(request), any(Long.class), any(UserIdAndRole.class));

        // when&then
        mockMvc.perform(patch("/managers/articles/issue/{issue_id}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andDo(document("issue-update",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                pathParameters(
                    parameterWithName("issue_id").description("이슈 id")
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

    @DisplayName("이슈 삭제 API 문서화")
    @Test
    @WithManager
    void deleteIssue() throws Exception {

        doNothing().when(articleIssueService)
            .deleteIssue(any(Long.class), any(UserIdAndRole.class));

        // when&then
        mockMvc.perform(delete("/managers/articles/issue/{issue_id}", 1))
            .andExpect(status().isOk())
            .andDo(document("issue-delete",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                pathParameters(
                    parameterWithName("issue_id").description("이슈 id")
                ),
                responseFields(
                    fieldWithPath("code").description("응답 상태 코드")
                )
            ));
    }

    @DisplayName("법정관리 이슈 히스토리 조회 API 문서화")
    @Test
    void testGetManageArticleIssueHistory() throws Exception {
        // given
        List<IssueHistoryResponseDto> issueHistory = asList(
            new IssueHistoryResponseDto(1L, "이슈제목 1", false, LocalDateTime.now()),
            new IssueHistoryResponseDto(2L, "이슈제목 2", true, LocalDateTime.now())
        );

        given(articleIssueService.getManageArticleIssueHistory(anyLong())).willReturn(issueHistory);

        // when & then
        mockMvc.perform(get("/managers/articles/manage/issues/{manageArticleId}", 1)
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(document("manage-issue-history",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                pathParameters(
                    parameterWithName("manageArticleId").description("법정관리 게시글 ID")
                ),
                responseFields(
                    fieldWithPath("code").description("응답 상태 코드"),
                    subsectionWithPath("data[]").description("데이터"),
                    fieldWithPath("data[].id").description("이슈 ID"),
                    fieldWithPath("data[].title").description("이슈 제목"),
                    fieldWithPath("data[].isActive").description("이슈 활성 상태"),
                    fieldWithPath("data[].createdAt").description("이슈 생성일")
                )
            ));
    }

    @DisplayName("유지보수 이슈 히스토리 조회 API 문서화")
    @Test
    void testGetRepairArticleIssueHistory() throws Exception {
        // given
        List<IssueHistoryResponseDto> issueHistory = asList(
            new IssueHistoryResponseDto(1L, "이슈제목 1", false, LocalDateTime.now()),
            new IssueHistoryResponseDto(2L, "이슈제목 2", true, LocalDateTime.now())
        );

        given(articleIssueService.getRepairArticleIssueHistory(anyLong())).willReturn(issueHistory);

        // when & then
        mockMvc.perform(get("/managers/articles/repair/issues/{repairArticleId}", 1)
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(document("repair-issue-history",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                pathParameters(
                    parameterWithName("repairArticleId").description("유지보수 게시글 ID")
                ),
                responseFields(
                    fieldWithPath("code").description("응답 상태 코드"),
                    subsectionWithPath("data[]").description("데이터"),
                    fieldWithPath("data[].id").description("이슈 ID"),
                    fieldWithPath("data[].title").description("이슈 제목"),
                    fieldWithPath("data[].isActive").description("이슈 활성 상태"),
                    fieldWithPath("data[].createdAt").description("이슈 생성일")
                )
            ));
    }
}

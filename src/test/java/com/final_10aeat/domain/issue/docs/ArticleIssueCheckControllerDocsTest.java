package com.final_10aeat.domain.issue.docs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.final_10aeat.common.util.member.WithMember;
import com.final_10aeat.docs.RestDocsSupport;
import com.final_10aeat.domain.articleIssue.controller.ArticleIssueCheckController;
import com.final_10aeat.domain.articleIssue.dto.request.ArticleIssueCheckRequestDto;
import com.final_10aeat.domain.articleIssue.dto.response.ArticleIssueCheckResponseDto;
import com.final_10aeat.domain.articleIssue.service.ArticleIssueCheckService;
import com.final_10aeat.domain.member.entity.Member;
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
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ArticleIssueCheckControllerDocsTest extends RestDocsSupport {


    private final ArticleIssueCheckService articleIssueCheckService = Mockito.mock(ArticleIssueCheckService.class);

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

    @DisplayName("유지보수 이슈 확인 API 문서화")
    @Test
    @WithMember
    void testManageIssueCheck() throws Exception {
        //given
        ArticleIssueCheckRequestDto requestDto = new ArticleIssueCheckRequestDto(true);
        ArticleIssueCheckResponseDto responseDto = new ArticleIssueCheckResponseDto(1L, "이슈 제목", "이슈 내용", true);

        // when
        when(articleIssueCheckService.manageIssueCheck(eq(requestDto), any(Long.class), any(Member.class)))
                .thenReturn(responseDto);

        // then
        mockMvc.perform(RestDocumentationRequestBuilders.post("/issues/check/manage/{manage_article_id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andDo(document("manage-issue-check",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("manage_article_id").description("유지보수 게시글 id")
                        ),
                        requestFields(
                                fieldWithPath("check").description("이슈 확인 여부")
                        ),
                        responseFields(
                                fieldWithPath("code").description("응답 상태 코드"),
                                fieldWithPath("data.id").description("이슈 id"),
                                fieldWithPath("data.title").description("이슈 제목"),
                                fieldWithPath("data.content").description("이슈 내용"),
                                fieldWithPath("data.check").description("이슈 확인 여부")
                        )
                ));
    }

    @DisplayName("유지관리 이슈 확인 API 문서화")
    @Test
    @WithMember
    void testRepairIssueCheck() throws Exception {
        //given
        ArticleIssueCheckRequestDto requestDto = new ArticleIssueCheckRequestDto(true);
        ArticleIssueCheckResponseDto responseDto = new ArticleIssueCheckResponseDto(1L, "이슈 제목", "이슈 내용", true);

        // when
        when(articleIssueCheckService.repairIssueCheck(eq(requestDto), any(Long.class), any(Member.class)))
                .thenReturn(responseDto);

        // then
        mockMvc.perform(RestDocumentationRequestBuilders.post("/issues/check/repair/{repair_article_id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andDo(document("repair-issue-check",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("repair_article_id").description("유지관리 게시글 id")
                        ),
                        requestFields(
                                fieldWithPath("check").description("이슈 확인 여부")
                        ),
                        responseFields(
                                fieldWithPath("code").description("응답 상태 코드"),
                                fieldWithPath("data.id").description("이슈 id"),
                                fieldWithPath("data.title").description("이슈 제목"),
                                fieldWithPath("data.content").description("이슈 내용"),
                                fieldWithPath("data.check").description("이슈 확인 여부")
                        )
                ));
    }

}

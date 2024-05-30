package com.final_10aeat.domain.save.docs;

import static org.mockito.Mockito.doNothing;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.final_10aeat.common.util.member.WithMember;
import com.final_10aeat.docs.RestDocsSupport;
import com.final_10aeat.domain.save.controller.ArticleSaveController;
import com.final_10aeat.domain.save.service.ArticleSaveService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;

public class ArticleSaveControllerDocsTest extends RestDocsSupport {

    private ArticleSaveService articleSaveService;

    @Override
    public Object initController() {
        articleSaveService = Mockito.mock(ArticleSaveService.class);
        return new ArticleSaveController(articleSaveService);
    }

    @DisplayName("게시글 저장 API 문서화")
    @Test
    @WithMember
    void testSaveArticle() throws Exception {
        // given
        Long repairArticleId = 1L;
        doNothing().when(articleSaveService).saveArticle(repairArticleId, 1L);

        // then
        mockMvc.perform(post("/repair/articles/save/{repairArticleId}", repairArticleId)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(document("save-article",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                pathParameters(
                    parameterWithName("repairArticleId").description("저장할 게시글 ID")
                ),
                responseFields(
                    fieldWithPath("code").description("응답 상태 코드")
                )
            ));
    }

    @DisplayName("게시글 저장 취소 API 문서화")
    @Test
    @WithMember
    void testUnsaveArticle() throws Exception {
        // given
        Long repairArticleId = 1L;
        doNothing().when(articleSaveService).unsaveArticle(repairArticleId, 1L);

        // then
        mockMvc.perform(delete("/repair/articles/save/{repairArticleId}", repairArticleId)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(document("unsave-article",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                pathParameters(
                    parameterWithName("repairArticleId").description("저장 취소할 게시글 ID")
                ),
                responseFields(
                    fieldWithPath("code").description("응답 상태 코드")
                )
            ));
    }
}

package com.final_10aeat.domain.repairArticle.docs;

import static org.mockito.Mockito.mock;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.final_10aeat.common.enumclass.ArticleCategory;
import com.final_10aeat.common.enumclass.Progress;
import com.final_10aeat.common.util.manager.WithManager;
import com.final_10aeat.docs.RestDocsSupport;
import com.final_10aeat.domain.repairArticle.controller.ManagerRepairArticleController;
import com.final_10aeat.domain.repairArticle.dto.request.CreateRepairArticleRequestDto;
import com.final_10aeat.domain.repairArticle.dto.request.UpdateRepairArticleRequestDto;
import com.final_10aeat.domain.repairArticle.service.RepairArticleService;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

public class ManagerRepairArticleDocsTest extends RestDocsSupport {

    @Override
    public Object initController() {
        RepairArticleService repairArticleService = mock(RepairArticleService.class);
        return new ManagerRepairArticleController(repairArticleService);
    }

    @DisplayName("Repair Article 생성 API 문서화")
    @Test
    @WithManager
    void testCreateRepairArticle() throws Exception {
        // given
        CreateRepairArticleRequestDto requestDto = new CreateRepairArticleRequestDto(
            ArticleCategory.REPAIR,
            Progress.PENDING,
            "유지보수 게시글 제목",
            "내용내용",
            LocalDateTime.now(),
            LocalDateTime.now().plusDays(5),
            "수리 회사 이름",
            "http://repaircompanywebsite.com",
            List.of("http://example.com/image1.jpg", "http://example.com/image2.jpg")
        );

        // when & then
        mockMvc.perform(post("/managers/repair/articles")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)))
            .andExpect(status().isOk())
            .andDo(document("create-repair-article",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestFields(
                    fieldWithPath("category").description("게시글 카테고리")
                        .description("INSTALL, REPAIR, REPLACE"),
                    fieldWithPath("progress").description("진행 상황")
                        .description("COMPLETE, INPROGRESS, PENDING"),
                    fieldWithPath("title").description("유지보수 게시글 제목"),
                    fieldWithPath("content").description("유지보수 게시글 내용"),
                    fieldWithPath("constructionStart").description("작업 시작 예정일").optional(),
                    fieldWithPath("constructionEnd").description("작업 종료 예정일").optional(),
                    fieldWithPath("repairCompany").description("수리 담당 회사의 이름").optional(),
                    fieldWithPath("repairCompanyWebsite").description("수리 회사 웹사이트 URL").optional(),
                    fieldWithPath("images").description("이미지 URL 목록").optional()
                ),
                responseFields(
                    fieldWithPath("code").description("응답 상태 코드")
                )
            ));
    }

    @DisplayName("Repair Article 삭제 API 문서화")
    @Test
    @WithManager
    void testDeleteRepairArticleById() throws Exception {
        // given

        // when & then
        mockMvc.perform(delete("/managers/repair/articles/{repairArticleId}", 1))
            .andExpect(status().isOk())
            .andDo(document("delete-repair-article",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                pathParameters(
                    parameterWithName("repairArticleId").description("유지보수 게시글 ID")
                ),
                responseFields(
                    fieldWithPath("code").description("응답 상태 코드")
                )
            ));
    }

    @DisplayName("Repair Article 수정 API 문서화")
    @Test
    @WithManager
    void testUpdateRepairArticleById() throws Exception {
        // given
        UpdateRepairArticleRequestDto requestDto = new UpdateRepairArticleRequestDto(
            ArticleCategory.REPAIR,
            Progress.PENDING,
            "유지보수 게시글 제목 수정",
            "내용 수정수정",
            LocalDateTime.now(),
            LocalDateTime.now().plusDays(5),
            "수리 회사 이름 수정",
            "http://updatedrepaircompanywebsite.com",
            List.of("http://example.com/image3.jpg", "http://example.com/image4.jpg")

        );

        // when & then
        mockMvc.perform(patch("/managers/repair/articles/{repairArticleId}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)))
            .andExpect(status().isOk())
            .andDo(document("update-repair-article",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                pathParameters(
                    parameterWithName("repairArticleId").description("유지보수 게시글 ID")
                ),
                requestFields(
                    fieldWithPath("category").description("게시글 카테고리")
                        .description("INSTALL, REPAIR, REPLACE").optional(),
                    fieldWithPath("progress").description("진행 상황")
                        .description("COMPLETE, INPROGRESS, PENDING").optional(),
                    fieldWithPath("title").description("유지보수 게시글 제목").optional(),
                    fieldWithPath("content").description("유지보수 게시글 내용").optional(),
                    fieldWithPath("constructionStart").description("작업 시작 예정일").optional(),
                    fieldWithPath("constructionEnd").description("작업 종료 예정일").optional(),
                    fieldWithPath("repairCompany").description("수리 담당 회사의 이름").optional(),
                    fieldWithPath("repairCompanyWebsite").description("수리 회사 웹사이트 URL").optional(),
                    fieldWithPath("images").description("이미지 URL 목록").optional()
                ),
                responseFields(
                    fieldWithPath("code").description("응답 상태 코드")
                )
            ));
    }
}

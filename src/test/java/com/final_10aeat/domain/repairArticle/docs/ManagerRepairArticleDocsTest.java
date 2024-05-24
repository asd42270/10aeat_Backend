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
import com.final_10aeat.domain.repairArticle.dto.request.CreateCustomProgressRequestDto;
import com.final_10aeat.domain.repairArticle.dto.request.CreateRepairArticleRequestDto;
import com.final_10aeat.domain.repairArticle.dto.request.UpdateCustomProgressRequestDto;
import com.final_10aeat.domain.repairArticle.dto.request.UpdateRepairArticleRequestDto;
import com.final_10aeat.domain.repairArticle.service.ManagerRepairArticleService;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

public class ManagerRepairArticleDocsTest extends RestDocsSupport {

    @Override
    public Object initController() {
        ManagerRepairArticleService repairArticleService = mock(ManagerRepairArticleService.class);
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
                    fieldWithPath("constructionStart").description("작업 시작 예정일"),
                    fieldWithPath("constructionEnd").description("작업 종료 예정일"),
                    fieldWithPath("repairCompany").description("수리 담당 회사의 이름"),
                    fieldWithPath("repairCompanyWebsite").description("수리 회사 웹사이트 URL"),
                    fieldWithPath("images").description("이미지 URL 목록")
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
                        .description("INSTALL, REPAIR, REPLACE"),
                    fieldWithPath("progress").description("진행 상황")
                        .description("COMPLETE, INPROGRESS, PENDING"),
                    fieldWithPath("title").description("유지보수 게시글 제목"),
                    fieldWithPath("content").description("유지보수 게시글 내용"),
                    fieldWithPath("constructionStart").description("작업 시작 예정일"),
                    fieldWithPath("constructionEnd").description("작업 종료 예정일"),
                    fieldWithPath("repairCompany").description("수리 담당 회사의 이름"),
                    fieldWithPath("repairCompanyWebsite").description("수리 회사 웹사이트 URL"),
                    fieldWithPath("images").description("이미지 URL 목록")
                ),
                responseFields(
                    fieldWithPath("code").description("응답 상태 코드")
                )
            ));
    }

    @DisplayName("안건진행사항 생성 API 문서화")
    @Test
    @WithManager
    void testCreateCustomProgress() throws Exception {
        // given
        CreateCustomProgressRequestDto requestDto = new CreateCustomProgressRequestDto(
            LocalDateTime.now(),
            LocalDateTime.now().plusDays(10),
            "안건진행사항 제목",
            "안건진행사항 내용"
        );

        // when & then
        mockMvc.perform(post("/managers/repair/articles/progress/{repairArticleId}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)))
            .andExpect(status().isOk())
            .andDo(document("create-custom-progress",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                pathParameters(
                    parameterWithName("repairArticleId").description("유지보수 게시글 ID")
                ),
                requestFields(
                    fieldWithPath("startSchedule").description("안건진행사항 시작 일정"),
                    fieldWithPath("endSchedule").description("안건진행사항 종료 일정"),
                    fieldWithPath("title").description("안건진행사항 제목"),
                    fieldWithPath("content").description("안건진행사항 내용")
                ),
                responseFields(
                    fieldWithPath("code").description("응답 상태 코드")
                )
            ));
    }

    @DisplayName("안건진행사항 수정 API 문서화")
    @Test
    @WithManager
    void testUpdateCustomProgress() throws Exception {
        // given
        UpdateCustomProgressRequestDto requestDto = new UpdateCustomProgressRequestDto(
            LocalDateTime.now(),
            LocalDateTime.now().plusDays(15),
            "안건진행사항 제목 수정",
            "안건진행사항 내용 수정",
            true
        );

        // when & then
        mockMvc.perform(patch("/managers/repair/articles/progress/{progressId}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)))
            .andExpect(status().isOk())
            .andDo(document("update-custom-progress",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                pathParameters(
                    parameterWithName("progressId").description("Custom Progress ID")
                ),
                requestFields(
                    fieldWithPath("startSchedule").description("안건진행사항 시작 일정"),
                    fieldWithPath("endSchedule").description("안건진행사항 종료 일정"),
                    fieldWithPath("title").description("안건진행사항 제목"),
                    fieldWithPath("content").description("안건진행사항 내용"),
                    fieldWithPath("inProgress").description("진행 중인 안건인지 여부")
                        .description("진행중 = true/ 종료,진행전 = false")
                ),
                responseFields(
                    fieldWithPath("code").description("응답 상태 코드")
                )
            ));
    }
}

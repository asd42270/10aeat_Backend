package com.final_10aeat.domain.repairArticle.docs;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.final_10aeat.common.dto.UserIdAndRole;
import com.final_10aeat.common.enumclass.ArticleCategory;
import com.final_10aeat.common.enumclass.Progress;
import com.final_10aeat.common.service.AuthenticationService;
import com.final_10aeat.docs.RestDocsSupport;
import com.final_10aeat.domain.repairArticle.controller.OwnerRepairArticleController;
import com.final_10aeat.domain.repairArticle.dto.response.CustomProgressResponseDto;
import com.final_10aeat.domain.repairArticle.dto.response.RepairArticleDetailDto;
import com.final_10aeat.domain.repairArticle.dto.response.RepairArticleResponseDto;
import com.final_10aeat.domain.repairArticle.dto.response.RepairArticleSummaryDto;
import com.final_10aeat.domain.repairArticle.service.OwnerRepairArticleService;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;

public class OwnerRepairArticleDocsTest extends RestDocsSupport {

    private OwnerRepairArticleService ownerRepairArticleService;
    private AuthenticationService authenticationService;

    @Override
    public Object initController() {
        ownerRepairArticleService = mock(OwnerRepairArticleService.class);
        authenticationService = mock(AuthenticationService.class);
        return new OwnerRepairArticleController(ownerRepairArticleService, authenticationService);
    }

    @DisplayName("유지보수 게시글 요약 조회 API 문서화")
    @Test
    void testGetRepairArticleSummary() throws Exception {
        UserIdAndRole userIdAndRole = new UserIdAndRole(1L, true);
        when(authenticationService.getCurrentUserIdAndRole()).thenReturn(userIdAndRole);
        when(authenticationService.getUserOfficeId()).thenReturn(1L);

        // given
        RepairArticleSummaryDto summaryDto = new RepairArticleSummaryDto(
            100L,
            60L,
            true,
            40L,
            false
        );

        when(ownerRepairArticleService.getRepairArticleSummary(1L)).thenReturn(summaryDto);

        // when & then
        mockMvc.perform(get("/repair/articles/summary")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(document("get-repair-article-summary",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                responseFields(
                    fieldWithPath("code").type(JsonFieldType.NUMBER).description("응답 상태 코드"),
                    fieldWithPath("message").type(JsonFieldType.STRING).optional()
                        .description("응답 메시지"),
                    fieldWithPath("data.total").type(JsonFieldType.NUMBER).description("전체 게시글 수"),
                    fieldWithPath("data.inProgressAndPending").type(JsonFieldType.NUMBER)
                        .description("진행/대기 게시글 수"),
                    fieldWithPath("data.inProgressAndPendingRedDot").type(JsonFieldType.BOOLEAN)
                        .description("진행/대기 게시글 이슈 여부"),
                    fieldWithPath("data.complete").type(JsonFieldType.NUMBER)
                        .description("완료 게시글 수"),
                    fieldWithPath("data.completeRedDot").type(JsonFieldType.BOOLEAN)
                        .description("완료 게시글 이슈 여부")
                )
            ));
    }

    @DisplayName("유지보수 게시글 전체 조회 API 문서화")
    @Test
    void testGetAllRepairArticles() throws Exception {
        UserIdAndRole userIdAndRole = new UserIdAndRole(1L, true);
        when(authenticationService.getCurrentUserIdAndRole()).thenReturn(userIdAndRole);
        when(authenticationService.getUserOfficeId()).thenReturn(1L);

        // given
        RepairArticleResponseDto articleDto1 = new RepairArticleResponseDto(
            1L, "REPAIR", "김관리자", "INPROGRESS", "제목1",
            LocalDateTime.now(), LocalDateTime.now().plusDays(5),
            LocalDateTime.now(), 5, 100, true, false, true,
            "http://example.com/image1.jpg"
        );

        RepairArticleResponseDto articleDto2 = new RepairArticleResponseDto(
            2L, "REPAIR", "이관리자", "PENDING", "제목2",
            LocalDateTime.now(), LocalDateTime.now().plusDays(10),
            LocalDateTime.now(), 3, 150, false, true, false,
            "http://example.com/image2.jpg"
        );

        List<RepairArticleResponseDto> articles = List.of(articleDto1, articleDto2);
        when(ownerRepairArticleService.getAllRepairArticles(userIdAndRole,
            List.of(Progress.INPROGRESS, Progress.PENDING), ArticleCategory.REPAIR)).thenReturn(
            articles);

        // when & then
        mockMvc.perform(get("/repair/articles/list")
                .param("progress", "INPROGRESS")
                .param("progress", "PENDING")
                .param("category", "REPAIR")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(document("get-repair-article-list",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                queryParameters(
                    parameterWithName("progress").description(
                        "진행상황 (INPROGRESS,PENDING / COMPLETE)").optional(),
                    parameterWithName("category").description("카테고리 (INSTALL / REPAIR / REPLACE)")
                        .optional()
                ),
                responseFields(
                    fieldWithPath("code").type(JsonFieldType.NUMBER).description("응답 상태 코드"),
                    fieldWithPath("message").type(JsonFieldType.STRING).optional()
                        .description("응답 메시지"),
                    fieldWithPath("data[].id").type(JsonFieldType.NUMBER).description("게시글 ID"),
                    fieldWithPath("data[].category").type(JsonFieldType.STRING)
                        .description("카테고리"),
                    fieldWithPath("data[].managerName").type(JsonFieldType.STRING)
                        .description("작성자 이름"),
                    fieldWithPath("data[].progress").type(JsonFieldType.STRING)
                        .description("진행 상태"),
                    fieldWithPath("data[].title").type(JsonFieldType.STRING).description("게시글 제목"),
                    fieldWithPath("data[].startConstruction").type(JsonFieldType.STRING)
                        .description("작업 시작 예정일"),
                    fieldWithPath("data[].endConstruction").type(JsonFieldType.STRING)
                        .description("작업 종료 예정일"),
                    fieldWithPath("data[].createdAt").type(JsonFieldType.STRING)
                        .description("게시글 생성일"),
                    fieldWithPath("data[].commentCount").type(JsonFieldType.NUMBER)
                        .description("댓글 수"),
                    fieldWithPath("data[].viewCount").type(JsonFieldType.NUMBER)
                        .description("조회 수"),
                    fieldWithPath("data[].isSave").type(JsonFieldType.BOOLEAN)
                        .description("게시글 저장 여부"),
                    fieldWithPath("data[].redDot").type(JsonFieldType.BOOLEAN)
                        .description("레드닷 표시 여부"),
                    fieldWithPath("data[].isNewArticle").type(JsonFieldType.BOOLEAN)
                        .description("24시간 내 생성된 게시글 여부"),
                    fieldWithPath("data[].imageUrl").type(JsonFieldType.STRING).optional()
                        .description("대표 이미지 URL")
                )
            ));
    }

    @DisplayName("유지보수 게시글 상세 조회 API 문서화")
    @Test
    void testGetRepairArticleDetail() throws Exception {
        UserIdAndRole userIdAndRole = new UserIdAndRole(1L, true);
        when(authenticationService.getCurrentUserIdAndRole()).thenReturn(userIdAndRole);

        // given
        RepairArticleDetailDto detailDto = new RepairArticleDetailDto(
            "REPAIR", "INPROGRESS", true, 1L, "김관리자",
            LocalDateTime.now(), LocalDateTime.now(),
            "유지보수 게시글 제목", "유지보수 게시글 내용",
            List.of("http://example.com/image1.jpg", "http://example.com/image2.jpg"),
            LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(5),
            "수리회사 이름", "http://repaircompany.com"
        );

        when(ownerRepairArticleService.getArticleDetails(1L, userIdAndRole.id(),
            userIdAndRole.isManager())).thenReturn(detailDto);

        // when & then
        mockMvc.perform(get("/repair/articles/{repairArticleId}", 1L)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(document("get-repair-article-detail",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                pathParameters(
                    parameterWithName("repairArticleId").description("조회할 게시글의 ID")
                ),
                responseFields(
                    fieldWithPath("code").type(JsonFieldType.NUMBER).description("응답 상태 코드"),
                    fieldWithPath("message").type(JsonFieldType.STRING).optional()
                        .description("응답 메시지"),
                    fieldWithPath("data.category").type(JsonFieldType.STRING)
                        .description("카테고리"),
                    fieldWithPath("data.progress").type(JsonFieldType.STRING)
                        .description("진행 상태"),
                    fieldWithPath("data.isSave").type(JsonFieldType.BOOLEAN)
                        .description("게시글 저장 여부"),
                    fieldWithPath("data.managerId").type(JsonFieldType.NUMBER)
                        .description("작성자(관리자) ID"),
                    fieldWithPath("data.managerName").type(JsonFieldType.STRING)
                        .description("관리자 이름"),
                    fieldWithPath("data.createdAt").type(JsonFieldType.STRING)
                        .description("게시글 생성일"),
                    fieldWithPath("data.updatedAt").type(JsonFieldType.STRING)
                        .description("게시글 수정일"),
                    fieldWithPath("data.title").type(JsonFieldType.STRING).description("게시글 제목"),
                    fieldWithPath("data.content").type(JsonFieldType.STRING).description("게시글 내용"),
                    fieldWithPath("data.imageUrls[]").type(JsonFieldType.ARRAY)
                        .description("게시글 이미지 URLs"),
                    fieldWithPath("data.startConstruction").type(JsonFieldType.STRING)
                        .description("작업 시작 예정일"),
                    fieldWithPath("data.endConstruction").type(JsonFieldType.STRING)
                        .description("작업 종료 예정일"),
                    fieldWithPath("data.company").type(JsonFieldType.STRING)
                        .description("수리 회사 이름"),
                    fieldWithPath("data.companyWebsite").type(JsonFieldType.STRING)
                        .description("수리 회사 웹사이트 URL")
                )
            ));
    }

    @DisplayName("안건진행사항 조회 API 문서화")
    @Test
    void testGetCustomProgressList() throws Exception {
        // given
        CustomProgressResponseDto progressDto1 = new CustomProgressResponseDto(
            1L, "진행사항 제목 1", "진행사항 내용 1", true,
            LocalDateTime.now(), LocalDateTime.now().plusDays(1)
        );

        CustomProgressResponseDto progressDto2 = new CustomProgressResponseDto(
            2L, "진행사항 제목 2", "진행사항 내용 2", false,
            LocalDateTime.now().minusDays(1), LocalDateTime.now().plusDays(2)
        );

        List<CustomProgressResponseDto> progressList = List.of(progressDto1, progressDto2);
        when(ownerRepairArticleService.getCustomProgressList(1L)).thenReturn(progressList);

        // when & then
        mockMvc.perform(get("/repair/articles/progress/{repairArticleId}", 1L)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(document("get-custom-progress-list",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                pathParameters(
                    parameterWithName("repairArticleId").description("조회할 게시글의 ID")
                ),
                responseFields(
                    fieldWithPath("code").type(JsonFieldType.NUMBER).description("응답 상태 코드"),
                    fieldWithPath("message").type(JsonFieldType.STRING).optional()
                        .description("응답 메시지"),
                    fieldWithPath("data[].id").type(JsonFieldType.NUMBER).description("진행사항 ID"),
                    fieldWithPath("data[].title").type(JsonFieldType.STRING).description("진행사항 제목"),
                    fieldWithPath("data[].content").type(JsonFieldType.STRING)
                        .description("진행사항 내용"),
                    fieldWithPath("data[].inProgress").type(JsonFieldType.BOOLEAN)
                        .description("진행 중 여부"),
                    fieldWithPath("data[].startSchedule").type(JsonFieldType.STRING)
                        .description("시작 일정"),
                    fieldWithPath("data[].endSchedule").type(JsonFieldType.STRING)
                        .description("종료 일정")
                )
            ));
    }
}

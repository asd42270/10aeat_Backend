package com.final_10aeat.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.JsonFieldType.NUMBER;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.final_10aeat.common.enumclass.ArticleCategory;
import com.final_10aeat.common.enumclass.ManagePeriod;
import com.final_10aeat.common.enumclass.Progress;
import com.final_10aeat.common.util.member.WithMember;
import com.final_10aeat.docs.RestDocsSupport;
import com.final_10aeat.domain.manageArticle.dto.response.SearchManagersManageResponse;
import com.final_10aeat.domain.repairArticle.dto.response.SearchManagerRepairArticleResponseDto;
import com.final_10aeat.usecase.SearchArticleUseCase;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;

public class ManagerSearchArticleControllerDocsTest extends RestDocsSupport {

    private SearchArticleUseCase searchArticleUseCase;

    @Override
    public Object initController() {
        searchArticleUseCase = Mockito.mock(SearchArticleUseCase.class);
        return new ManagerSearchArticleController(searchArticleUseCase);
    }

    @DisplayName("유지보수 게시글 검색 API 문서화")
    @Test
    void testRepairArticleSearch() throws Exception {
        // given
        SearchManagerRepairArticleResponseDto dto = new SearchManagerRepairArticleResponseDto(
            2L, ArticleCategory.REPAIR, "김관리자", Progress.INPROGRESS, "제목1",
            LocalDateTime.of(2024, 6, 6, 0, 0),
            LocalDateTime.of(2024, 6, 11, 0, 0),
            LocalDateTime.of(2024, 5, 1, 0, 0),
            LocalDateTime.of(2024, 5, 2, 0, 0),
            7, 100
        );

        given(searchArticleUseCase.managerSearchRepair(any(), any(), any()))
            .willReturn(new PageImpl<>(List.of(dto)));

        // then
        mockMvc.perform(get("/managers/repair/articles")
                .param("keyword", "제목")
                .param("progress", "INPROGRESS")
                .param("page", "0")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(document("manager-repair-article-search",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                queryParameters(
                    parameterWithName("keyword").description("검색어, null인 경우 전체 조회").optional(),
                    parameterWithName("progress").description("진행 상태, null인 경우 전체 조회").optional(),
                    parameterWithName("page").description("페이지 번호").optional()
                ),
                responseFields(
                    fieldWithPath("code").type(JsonFieldType.NUMBER).description("응답 상태 코드"),
                    fieldWithPath("data.pageSize").type(JsonFieldType.NUMBER).description("페이지 크기"),
                    fieldWithPath("data.currentPage").type(JsonFieldType.NUMBER)
                        .description("현재 페이지 번호"),
                    fieldWithPath("data.totalElements").type(JsonFieldType.NUMBER)
                        .description("전체 항목 수"),
                    fieldWithPath("data.totalPages").type(JsonFieldType.NUMBER)
                        .description("전체 페이지 수"),
                    fieldWithPath("data.articles[].id").type(JsonFieldType.NUMBER)
                        .description("게시글 ID"),
                    fieldWithPath("data.articles[].category").type(JsonFieldType.STRING)
                        .description("카테고리"),
                    fieldWithPath("data.articles[].managerName").type(JsonFieldType.STRING)
                        .description("작성자 이름"),
                    fieldWithPath("data.articles[].progress").type(JsonFieldType.STRING)
                        .description("진행 상태"),
                    fieldWithPath("data.articles[].title").type(JsonFieldType.STRING)
                        .description("게시글 제목"),
                    fieldWithPath("data.articles[].startConstruction").type(JsonFieldType.STRING)
                        .description("작업 시작 예정일"),
                    fieldWithPath("data.articles[].endConstruction").type(JsonFieldType.STRING)
                        .description("작업 종료 예정일"),
                    fieldWithPath("data.articles[].createdAt").type(JsonFieldType.STRING)
                        .description("게시글 생성일"),
                    fieldWithPath("data.articles[].updatedAt").type(JsonFieldType.STRING)
                        .description("게시글 수정일"),
                    fieldWithPath("data.articles[].commentCount").type(JsonFieldType.NUMBER)
                        .description("댓글 수"),
                    fieldWithPath("data.articles[].viewCount").type(JsonFieldType.NUMBER)
                        .description("조회 수")
                )
            ));
    }

    @DisplayName("법정점검 게시글 검색 API 문서화")
    @Test
    @WithMember
    void testManageArticleSearch() throws Exception {
        // given
        List<SearchManagersManageResponse> dto = List.of(
            SearchManagersManageResponse.builder()
                .id(1L)
                .period(ManagePeriod.YEAR)
                .periodCount(12)
                .title("제목")
                .allSchedule(12)
                .completedSchedule(6)
                .currentSchedules(
                    LocalDateTime.of(2024, 6, 6, 0, 0)
                )
                .build()
        );

        given(searchArticleUseCase.managerSearchManage(any(), any(), any(), any(), any()))
            .willReturn(new PageImpl<>(dto));

        // when & then
        mockMvc.perform(get("/managers/manage/articles")
                .param("keyword", "제목")
                .param("page", "0")
                .param("year", "2024")
                .param("month", "1")
                .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isOk())
            .andDo(document("manager-manage-article-search",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    queryParameters(
                        parameterWithName("keyword").description("검색어").optional(),
                        parameterWithName("page").description("페이지 번호").optional(),
                        parameterWithName("year").description("검색 할 년도, null인 경우 전체 검색").optional(),
                        parameterWithName("month").description("검색 할 월, null인 경우 전체 검색").optional()
                    ),
                    responseFields(
                        fieldWithPath("code").type(JsonFieldType.NUMBER).description("응답 상태 코드"),
                        fieldWithPath("data.pageSize").type(JsonFieldType.NUMBER).description("페이지 크기"),
                        fieldWithPath("data.currentPage").type(JsonFieldType.NUMBER)
                            .description("현재 페이지 번호"),
                        fieldWithPath("data.totalElements").type(JsonFieldType.NUMBER)
                            .description("전체 항목 수"),
                        fieldWithPath("data.totalPages").type(JsonFieldType.NUMBER)
                            .description("전체 페이지 수"),
                        fieldWithPath("data.articles[].id").type(NUMBER).description("점검  ID"),
                        fieldWithPath("data.articles[].period").type(STRING).description("점검 주기"),
                        fieldWithPath("data.articles[].periodCount").type(NUMBER).description("주기별 횟수"),
                        fieldWithPath("data.articles[].title").type(STRING).description("제목"),
                        fieldWithPath("data.articles[].allSchedule").type(NUMBER)
                            .description("전체 일정 수"),
                        fieldWithPath("data.articles[].completedSchedule").type(NUMBER)
                            .description("완료된 일정 수"),
                        fieldWithPath("data.articles[].currentSchedules").type(STRING)
                            .description("아직 지나지 않은 가장 가까운 일정")
                    )
                )
            );
    }
}
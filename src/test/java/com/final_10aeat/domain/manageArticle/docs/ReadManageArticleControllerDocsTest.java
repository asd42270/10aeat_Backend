package com.final_10aeat.domain.manageArticle.docs;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.JsonFieldType.ARRAY;
import static org.springframework.restdocs.payload.JsonFieldType.BOOLEAN;
import static org.springframework.restdocs.payload.JsonFieldType.NUMBER;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.final_10aeat.common.enumclass.ManagePeriod;
import com.final_10aeat.common.enumclass.Progress;
import com.final_10aeat.common.service.AuthenticationService;
import com.final_10aeat.common.util.member.WithMember;
import com.final_10aeat.docs.RestDocsSupport;
import com.final_10aeat.domain.manageArticle.controller.ReadManageArticleController;
import com.final_10aeat.domain.manageArticle.dto.response.DetailManageArticleResponse;
import com.final_10aeat.domain.manageArticle.dto.response.ListManageArticleResponse;
import com.final_10aeat.domain.manageArticle.dto.response.ManageArticleSummaryResponse;
import com.final_10aeat.domain.manageArticle.dto.response.ManageScheduleResponse;
import com.final_10aeat.domain.manageArticle.dto.response.SummaryManageArticleResponse;
import com.final_10aeat.domain.manageArticle.service.ReadManageArticleService;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

public class ReadManageArticleControllerDocsTest extends RestDocsSupport {

    private ReadManageArticleService readManageArticleService;
    private AuthenticationService authenticationService;

    @Override
    public Object initController() {
        readManageArticleService = mock(ReadManageArticleService.class);
        authenticationService = mock(AuthenticationService.class);
        return new ReadManageArticleController(readManageArticleService, authenticationService);
    }

    @DisplayName("manageArticle 요약 API 문서화")
    @Test
    @WithMember
    void testCreate() throws Exception {
        // given
        SummaryManageArticleResponse response = new SummaryManageArticleResponse(
            1,
            1,
            1,
            List.of(1L, 2L)
        );

        given(authenticationService.getUserOfficeId()).willReturn(1L);
        given(readManageArticleService.summary(anyLong())).willReturn(response);

        // when & then
        mockMvc.perform(get("/manage/articles/summary")
                .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isOk())
            .andDo(document("get-manage-article-summary",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    responseFields(
                        fieldWithPath("code").type(NUMBER).description("응답 상태 코드"),
                        fieldWithPath("data.complete").type(NUMBER).description("완료된 사항 수"),
                        fieldWithPath("data.inprogress").type(NUMBER).description("진행 중인 사항 수"),
                        fieldWithPath("data.pending").type(NUMBER).description("대기 중인 사항 수"),
                        fieldWithPath("data.hasIssue").type(ARRAY).description("이슈 id 목록")
                    )
                )
            );
    }

    @DisplayName("manageArticle 상세 API 문서화")
    @Test
    @WithMember
    void testDetail() throws Exception {
        // given
        ManageScheduleResponse scheduleResponse1 = ManageScheduleResponse.builder()
            .manageScheduleId(1L)
            .isComplete(true)
            .scheduleStart(
                LocalDateTime.of(2022, 4, 5, 0, 0, 0)
            )
            .scheduleEnd(
                LocalDateTime.of(2022, 4, 5, 0, 0, 0)
            )
            .build();
        ManageScheduleResponse scheduleResponse2 = ManageScheduleResponse.builder()
            .manageScheduleId(2L)
            .isComplete(false)
            .scheduleStart(
                LocalDateTime.of(2022, 3, 5, 0, 0, 0)
            )
            .scheduleEnd(
                LocalDateTime.of(2022, 5, 5, 0, 0, 0)
            )
            .build();

        DetailManageArticleResponse response = DetailManageArticleResponse.builder()
            .period(ManagePeriod.YEAR)
            .periodCount(1)
            .title("제목")
            .issueId(32L)
            .progress(Progress.COMPLETE)
            .legalBasis("법적근거")
            .target("대상")
            .responsibility("담당 업체")
            .note("비고")
            .manageSchedule(
                List.of(
                    scheduleResponse1, scheduleResponse2
                )
            )
            .build();

        given(authenticationService.getUserOfficeId()).willReturn(1L);
        given(readManageArticleService.detailArticle(anyLong(), anyLong())).willReturn(response);

        // when & then
        mockMvc.perform(get("/manage/articles/{manageArticleId}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isOk())
            .andDo(document("get-manage-article-detail",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                pathParameters(
                    parameterWithName("manageArticleId").description("조회할 게시글 id")
                ),
                responseFields(
                    fieldWithPath("code").type(NUMBER).description("응답 상태 코드"),
                    fieldWithPath("data.period").type(STRING).description("점검 주기"),
                    fieldWithPath("data.periodCount").type(NUMBER).description("주기별 횟수"),
                    fieldWithPath("data.title").type(STRING).description("제목"),
                    fieldWithPath("data.issueId").type(NUMBER).description("이슈 id"),
                    fieldWithPath("data.progress").type(STRING).description("진행 상태"),
                    fieldWithPath("data.legalBasis").type(STRING).description("법적 근거"),
                    fieldWithPath("data.target").type(STRING).description("대상"),
                    fieldWithPath("data.responsibility").type(STRING).description("담당 업체"),
                    fieldWithPath("data.note").type(STRING).description("비고"),
                    fieldWithPath("data.manageSchedule").type(ARRAY).description("관리 일정 목록"),
                    fieldWithPath("data.manageSchedule[].manageScheduleId").type(NUMBER)
                        .description("관리 일정 ID"),
                    fieldWithPath("data.manageSchedule[].isComplete").type(BOOLEAN)
                        .description("완료 여부"),
                    fieldWithPath("data.manageSchedule[].scheduleStart").type(STRING)
                        .description("일정 시작 시간"),
                    fieldWithPath("data.manageSchedule[].scheduleEnd").type(STRING)
                        .description("일정 종료 시간")
                )
            ));
    }

    @DisplayName("manageArticle 목록 API 문서화")
    @Test
    @WithMember
    void testList() throws Exception {
        // given
        List<ListManageArticleResponse> responseList = List.of(
            ListManageArticleResponse.builder()
                .id(1L)
                .period(ManagePeriod.YEAR)
                .periodCount(12)
                .title("제목")
                .allSchedule(12)
                .completedSchedule(6)
                .issueId(1L)
                .build(),
            ListManageArticleResponse.builder()
                .id(2L)
                .period(ManagePeriod.MONTH)
                .periodCount(3)
                .title("제목")
                .allSchedule(3)
                .completedSchedule(1)
                .issueId(33L)
                .build()
        );

        given(authenticationService.getUserOfficeId()).willReturn(1L);
        given(readManageArticleService.listArticle(anyInt(), anyLong(), any())).willReturn(
            responseList);

        // when & then
        mockMvc.perform(get("/manage/articles/list")
                .contentType(MediaType.APPLICATION_JSON)
                .param("year", "2024")
                .param("complete", "true")
            )
            .andExpect(status().isOk())
            .andDo(document("get-manage-article-list",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    pathParameters(
                        parameterWithName("year").description("일정이 있는 연도, null인 경우 당 년도").optional(),
                        parameterWithName("complete").description(
                            "true = 완료, false = 진행중&대기, null인 경우 전체 검색").optional()
                    ),
                    responseFields(
                        fieldWithPath("code").type(NUMBER).description("응답 상태 코드"),
                        fieldWithPath("data[].id").type(NUMBER).description("점검  ID"),
                        fieldWithPath("data[].period").type(STRING).description("점검 주기"),
                        fieldWithPath("data[].periodCount").type(NUMBER).description("주기별 횟수"),
                        fieldWithPath("data[].title").type(STRING).description("제목"),
                        fieldWithPath("data[].allSchedule").type(NUMBER).description("전체 일정 수"),
                        fieldWithPath("data[].completedSchedule").type(NUMBER).description("완료된 일정 수"),
                        fieldWithPath("data[].issueId").type(NUMBER).description("이슈 ID")
                    )
                )
            );
    }

    @DisplayName("manageArticle 월별 요약 API 문서화")
    @Test
    @WithMember
    void testMonthlySummary() throws Exception {
        // given
        List<ManageArticleSummaryResponse> response =
            List.of(
                new ManageArticleSummaryResponse(1, 1),
                new ManageArticleSummaryResponse(3, 2),
                new ManageArticleSummaryResponse(6, 5)
            );

        given(authenticationService.getUserOfficeId()).willReturn(1L);
        given(readManageArticleService.monthlySummary(anyInt(), anyLong()))
            .willReturn(response);

        // when & then
        mockMvc.perform(get("/manage/articles/monthly/summary")
                .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isOk())
            .andDo(document("get-manage-article-monthly-summary",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    pathParameters(
                        parameterWithName("year").description("일정이 있는 연도, null인 경우 당 년도").optional()
                    ),
                    responseFields(
                        fieldWithPath("code").type(NUMBER).description("응답 상태 코드"),
                        fieldWithPath("data").type(ARRAY).description("응답 리스트"),
                        fieldWithPath("data[].month").type(NUMBER).description("데이터가 존재하는 월"),
                        fieldWithPath("data[].total").type(NUMBER).description("해당 월의 게시글 갯수")
                    )
                )
            );
    }

    @DisplayName("manageArticle 월별 목록 API 문서화")
    @Test
    @WithMember
    void testMonthlyList() throws Exception {
        // given
        List<ListManageArticleResponse> responseList = List.of(
            ListManageArticleResponse.builder()
                .id(1L)
                .period(ManagePeriod.YEAR)
                .periodCount(12)
                .title("제목")
                .allSchedule(12)
                .completedSchedule(6)
                .issueId(1L)
                .build()
        );

        given(authenticationService.getUserOfficeId()).willReturn(1L);
        given(readManageArticleService.monthlyListArticle(anyLong(), anyInt(), any()))
            .willReturn(responseList);

        // when & then
        mockMvc.perform(get("/manage/articles/monthly/list")
                .param("year", "2024")
                .param("month", "1")
                .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isOk())
            .andDo(document("get-manage-article-monthly-list",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                pathParameters(
                    parameterWithName("year").description("일정이 있는 연도, null인 경우 당 년도").optional(),
                    parameterWithName("month").description("검색할 월, null인 경우 전체 월").optional()
                ),
                responseFields(
                    fieldWithPath("code").type(NUMBER).description("응답 상태 코드"),
                    fieldWithPath("data[].id").type(NUMBER).description("점검  ID"),
                    fieldWithPath("data[].period").type(STRING).description("점검 주기"),
                    fieldWithPath("data[].periodCount").type(NUMBER).description("주기별 횟수"),
                    fieldWithPath("data[].title").type(STRING).description("제목"),
                    fieldWithPath("data[].allSchedule").type(NUMBER).description("전체 일정 수"),
                    fieldWithPath("data[].completedSchedule").type(NUMBER).description("완료된 일정 수"),
                    fieldWithPath("data[].issueId").type(NUMBER).description("이슈 ID")
                )
            ));
    }

}

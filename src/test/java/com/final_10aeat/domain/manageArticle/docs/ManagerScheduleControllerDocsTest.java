package com.final_10aeat.domain.manageArticle.docs;

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

import com.final_10aeat.common.util.manager.WithManager;
import com.final_10aeat.docs.RestDocsSupport;
import com.final_10aeat.domain.manageArticle.controller.ManagerScheduleController;
import com.final_10aeat.domain.manageArticle.dto.request.ScheduleRequestDto;
import com.final_10aeat.domain.manageArticle.service.ManageScheduleService;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

public class ManagerScheduleControllerDocsTest extends RestDocsSupport {

    @Override
    public Object initController() {
        ManageScheduleService service = mock(ManageScheduleService.class);
        return new ManagerScheduleController(service);
    }

    @DisplayName("Schedule 생성 API 문서화")
    @Test
    @WithManager
    void testCreate() throws Exception {
        // given
        ScheduleRequestDto request = new ScheduleRequestDto(
            LocalDateTime.of(2024, 6, 1, 0, 0),
            LocalDateTime.of(2024, 6, 14, 0, 0)
        );

        // when & then
        mockMvc.perform(post("/managers/manage/articles/schedules/{manageArticleId}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andDo(document("create-schedule",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestFields(
                    fieldWithPath("scheduleStart").description("일정 시작 날짜"),
                    fieldWithPath("scheduleEnd").description("일정 종료 날짜").optional()
                ),
                responseFields(
                    fieldWithPath("code").description("응답 상태 코드")
                )
            ));
    }

    @DisplayName("Schedule 완료 API 문서화")
    @Test
    @WithManager
    void testComplete() throws Exception {
        // when & then
        mockMvc.perform(post("/managers/manage/articles/schedules/progress/{manageScheduleId}", 1L))
            .andExpect(status().isOk())
            .andDo(document("complete-schedule",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                pathParameters(
                    parameterWithName("manageScheduleId").description("완료처리할 일정 ID")
                ),
                responseFields(
                    fieldWithPath("code").description("응답 상태 코드")
                )
            ));
    }

    @DisplayName("Schedule 일정 변경 API 문서화")
    @Test
    @WithManager
    void testUpdate() throws Exception {
        // given
        ScheduleRequestDto request = new ScheduleRequestDto(
            LocalDateTime.of(2024, 6, 1, 0, 0),
            LocalDateTime.of(2024, 6, 14, 0, 0)
        );

        // when & then
        mockMvc.perform(patch("/managers/manage/articles/schedules/progress/{manageScheduleId}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andDo(document("update-schedule",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestFields(
                    fieldWithPath("scheduleStart").description("일정 시작 날짜"),
                    fieldWithPath("scheduleEnd").description("일정 종료 날짜").optional()
                ),
                pathParameters(
                    parameterWithName("manageScheduleId").description("변경할 일정 ID")
                ),
                responseFields(
                    fieldWithPath("code").description("응답 상태 코드")
                )
            ));
    }

    @DisplayName("Schedule 삭제 API 문서화")
    @Test
    @WithManager
    void testDelete() throws Exception {
        // when & then
        mockMvc.perform(delete("/managers/manage/articles/schedules/progress/{manageScheduleId}", 1L))
            .andExpect(status().isOk())
            .andDo(document("delete-schedule",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                pathParameters(
                    parameterWithName("manageScheduleId").description("삭제할 일정 ID")
                ),
                responseFields(
                    fieldWithPath("code").description("응답 상태 코드")
                )
            ));
    }
}

package com.final_10aeat.domain.alarm.docs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.final_10aeat.common.dto.UserIdAndRole;
import com.final_10aeat.common.service.AuthenticationService;
import com.final_10aeat.common.util.member.WithMember;
import com.final_10aeat.docs.RestDocsSupport;
import com.final_10aeat.domain.alarm.controller.AlarmController;
import com.final_10aeat.domain.alarm.dto.response.AlarmResponseDto;
import com.final_10aeat.domain.alarm.entity.enumtype.AlarmType;
import com.final_10aeat.domain.alarm.service.AlarmService;
import com.final_10aeat.domain.member.entity.Member;
import com.final_10aeat.global.util.ResponseDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AlarmControllerDocsTest extends RestDocsSupport {

    private AlarmService alarmService;
    private AuthenticationService authenticationService;

    @Override
    public Object initController() {
        alarmService = Mockito.mock(AlarmService.class);
        authenticationService = Mockito.mock(AuthenticationService.class);
        objectMapper = new ObjectMapper();
        return new AlarmController(alarmService, authenticationService);
    }

    @DisplayName("알림 요청 성공 API 문서화")
    @Test
    @WithMember
    void _testGetAlarmSuccess() throws Exception {
        // given
        Member member = Member.builder().id(1L).build();
        AlarmResponseDto response = AlarmResponseDto.builder()
                .alarmId(1L)
                .alarmType(AlarmType.ANSWER)
                .targetId(1L)
                .checked(false)
                .build();

        DeferredResult<ResponseEntity<ResponseDTO<AlarmResponseDto>>> result = new DeferredResult<>();
        result.setResult(ResponseEntity.ok(ResponseDTO.okWithData(response)));

        // when
        when(alarmService.getDeferredResult(member.getId())).thenReturn(result);

        MvcResult mvcResult = mockMvc.perform(get("/alarm"))
                .andExpect(status().isOk())
                .andReturn();

        // then
        mockMvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isOk())
                .andDo(document("get-alarm-success",
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("code").description("응답상태 코드"),
                                fieldWithPath("data.alarmId").description("알림 id"),
                                fieldWithPath("data.alarmType").description("알림 유형"),
                                fieldWithPath("data.targetId").description("알림 내용 id"),
                                fieldWithPath("data.checked").description("확인 여부")
                        )
                ));
    }

    @DisplayName("알림 요청 실패 API 문서화")
    @Test
    @WithMember
    void _testGetAlarmFail() throws Exception {
        // given
        Member member = Member.builder().id(1L).build();

        DeferredResult<ResponseEntity<ResponseDTO<AlarmResponseDto>>> result = new DeferredResult<>();
        result.setErrorResult(ResponseEntity.status(HttpStatus.NO_CONTENT)
                .body(ResponseDTO.ok()));

        // when
        when(alarmService.getDeferredResult(member.getId())).thenReturn(result);

        MvcResult mvcResult = mockMvc.perform(get("/alarm"))
                .andExpect(request().asyncStarted())
                .andReturn();


        mockMvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isNoContent())
                .andDo(document("get-alarm-fail",
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("code").description("응답상태 데이터")
                        )
                ));
    }

    @DisplayName("알림 히스토리 조회 API 문서화")
    @Test
    @WithMember
    void _testGetAlarmHistory() throws Exception {
        // given
        AlarmResponseDto responseDto1 = new AlarmResponseDto(1L, AlarmType.ANSWER, 1L, false);
        AlarmResponseDto responseDto2 = new AlarmResponseDto(2L, AlarmType.SAVED, 1L, true);

        List<AlarmResponseDto> response = List.of(responseDto1, responseDto2);

        UserIdAndRole userIdAndRole = new UserIdAndRole(1L, false);

        // when
        when(authenticationService.getCurrentUserIdAndRole()).thenReturn(userIdAndRole);
        when(alarmService.getAlarmHistory(any(UserIdAndRole.class))).thenReturn(response);

        // then
        mockMvc.perform(get("/alarm/history")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("alarm-history",
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("code").description("응답상태 코드"),
                                fieldWithPath("data[].alarmId").description("알림 id"),
                                fieldWithPath("data[].alarmType").description("알림 유형"),
                                fieldWithPath("data[].targetId").description("알림 내용 id"),
                                fieldWithPath("data[].checked").description("확인 여부")
                        )
                ));
    }

    @DisplayName("알림 확인 API 문서화")
    @Test
    @WithMember
    void _checkedAlarm() throws Exception {
        // given
        UserIdAndRole userIdAndRole = new UserIdAndRole(1L, false);

        // when
        when(authenticationService.getCurrentUserIdAndRole()).thenReturn(userIdAndRole);
        doNothing().when(alarmService).checkedAlarm(any(Long.class), any(UserIdAndRole.class));

        // then
        mockMvc.perform(post("/alarm/{alarm_id}", 1L))
                .andExpect(status().isOk())
                .andDo(document("alarm-check",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("alarm_id").description("알림 id")
                        ),
                        responseFields(
                                fieldWithPath("code").description("응답 상태 코드")
                        )
                ));
    }
}

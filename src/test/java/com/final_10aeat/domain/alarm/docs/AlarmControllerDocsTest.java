package com.final_10aeat.domain.alarm.docs;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.context.request.async.DeferredResult;

import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch;
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
                .andDo(document("get-alarm",
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
                .andExpect(status().isOk())
                .andReturn();


        mockMvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isNoContent())
                .andDo(document("get-alarm",
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("code").description("응답상태 데이터")
                        )
                ));

    }


}

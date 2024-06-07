package com.final_10aeat.domain.alarm.unit;

import com.final_10aeat.domain.alarm.dto.response.AlarmResponseDto;
import com.final_10aeat.domain.alarm.service.AlarmService;
import com.final_10aeat.global.util.ResponseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.event.RecordApplicationEvents;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


@RecordApplicationEvents
public class GetDeferredResultServiceTest {

    @InjectMocks
    private AlarmService alarmService;

    @BeforeEach
    public void setUp()  { MockitoAnnotations.openMocks(this); }

    @Nested
    @DisplayName("getDeferredResult()는")
    class Context_getDeferredResult{

        @DisplayName("DeferredResult 반환에 성공한다.")
        @Test
        void _willSuccess() {
            // given
            Long memberId = 1L;

            // when&then
            assertThat(alarmService.getDeferredResult(memberId)).isNotNull();
        }

        @DisplayName("타임 아웃이 발생한다.")
        @Test
        void _willTimeOut() {
            // given
            Long memberId = 1L;

            //when
            DeferredResult<ResponseEntity<ResponseDTO<AlarmResponseDto>>> result = alarmService.getDeferredResult(memberId);
            result.setErrorResult(ResponseEntity.status(HttpStatus.NO_CONTENT).body(ResponseDTO.ok()));

            // then
            result.onTimeout(() -> {
                assertTrue(result.hasResult());
                assertEquals(HttpStatus.NO_CONTENT, ((ResponseEntity<ResponseDTO<AlarmResponseDto>>) Objects.requireNonNull(result.getResult())).getStatusCode());
            });
        }
    }
}

package com.final_10aeat.domain.alarm.unit;

import com.final_10aeat.common.enumclass.MemberRole;
import com.final_10aeat.domain.alarm.dto.response.AlarmResponseDto;
import com.final_10aeat.domain.alarm.entity.Alarm;
import com.final_10aeat.domain.alarm.entity.enumtype.AlarmType;
import com.final_10aeat.domain.alarm.event.BookmarkCommentEvent;
import com.final_10aeat.domain.alarm.event.CommentEvent;
import com.final_10aeat.domain.alarm.repository.AlarmRepository;
import com.final_10aeat.domain.alarm.service.AlarmService;
import com.final_10aeat.domain.member.entity.Member;
import com.final_10aeat.global.util.ResponseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.Collections;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

public class EventHandlerServiceTest {

    @Mock
    private AlarmRepository alarmRepository;
    @Mock
    private ConcurrentHashMap<Long, DeferredResult<ResponseEntity<ResponseDTO<AlarmResponseDto>>>> alarmMap = new ConcurrentHashMap<>();
    @InjectMocks
    private AlarmService alarmService;

    private final Member member = Member.builder()
            .id(1L)
            .email("test@test.com")
            .password("encodedPassword")
            .name("spring")
            .role(MemberRole.OWNER)
            .build();

    private final CommentEvent commentEvent = CommentEvent.builder()
            .commentId(1L)
            .member(member)
            .build();

    private final BookmarkCommentEvent bookmarkEvent = BookmarkCommentEvent.builder()
            .articleId(1L)
            .members(Collections.singletonList(member))
            .build();

    private final Alarm alarm = Alarm.builder()
            .id(1L)
            .checked(false)
            .alarmType(AlarmType.ANSWER)
            .targetId(commentEvent.commentId())
            .member(member)
            .build();

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Nested
    @DisplayName("commentEventHandler()는")
    class Context_commentEventHandler {

        @DisplayName("이벤트를 수신하여 DeferredResult에 결과를 저장한다.")
        @Test
        void _willSuccess() {
            // given
            DeferredResult<ResponseEntity<ResponseDTO<AlarmResponseDto>>> deferredResult = alarmService.getDeferredResult(member.getId());
            given(alarmMap.get(member.getId())).willReturn(deferredResult);
            given(alarmRepository.save(any(Alarm.class))).willReturn(alarm);

            // when
            alarmService.commentEventHandler(commentEvent);

            // then
            ResponseEntity<ResponseDTO<AlarmResponseDto>> result = (ResponseEntity<ResponseDTO<AlarmResponseDto>>) deferredResult.getResult();
            assertThat(deferredResult.getResult()).isNotNull();
            assertEquals(Objects.requireNonNull(Objects.requireNonNull(result).getBody()).getData().alarmId(), alarm.getId());
        }
    }

    @DisplayName("bookMarkEventHandler()는")
    @Nested
    class Context_bookMarkEventHandler {

        @DisplayName("이벤트를 수신하여 DeferredResult에 결과를 저장한다.")
        @Test
        void _willSuccess() {
            // given
            DeferredResult<ResponseEntity<ResponseDTO<AlarmResponseDto>>> deferredResult = alarmService.getDeferredResult(member.getId());
            given(alarmMap.get(member.getId())).willReturn(deferredResult);
            given(alarmRepository.save(any(Alarm.class))).willReturn(alarm);

            // when
            alarmService.bookMarkEventHandler(bookmarkEvent);

            //then
            ResponseEntity<ResponseDTO<AlarmResponseDto>> result = (ResponseEntity<ResponseDTO<AlarmResponseDto>>) deferredResult.getResult();
            assertThat(deferredResult.getResult()).isNotNull();
            assertEquals(Objects.requireNonNull(Objects.requireNonNull(result).getBody()).getData().alarmId(), alarm.getId());
        }
    }
}

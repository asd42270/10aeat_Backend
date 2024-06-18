package com.final_10aeat.domain.alarm.service;

import com.final_10aeat.common.dto.UserIdAndRole;
import com.final_10aeat.domain.alarm.dto.response.AlarmResponseDto;
import com.final_10aeat.domain.alarm.entity.Alarm;
import com.final_10aeat.domain.alarm.entity.enumtype.AlarmType;
import com.final_10aeat.domain.alarm.event.SavedArticleCommentEvent;
import com.final_10aeat.domain.alarm.event.CommentEvent;
import com.final_10aeat.domain.alarm.exception.AlarmNotFoundException;
import com.final_10aeat.domain.alarm.repository.AlarmRepository;
import com.final_10aeat.domain.member.entity.Member;
import com.final_10aeat.domain.member.exception.UserNotExistException;
import com.final_10aeat.domain.member.repository.MemberRepository;
import com.final_10aeat.global.util.ResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
@EnableAsync
@Slf4j
public class AlarmService {

    private final AlarmRepository alarmRepository;
    private final MemberRepository memberRepository;
    private final ConcurrentHashMap<Long, DeferredResult<ResponseEntity<ResponseDTO<AlarmResponseDto>>>> alarmMap = new ConcurrentHashMap<>();

    public DeferredResult<ResponseEntity<ResponseDTO<AlarmResponseDto>>> getDeferredResult(Long memberId) {
        DeferredResult<ResponseEntity<ResponseDTO<AlarmResponseDto>>> deferredResult = new DeferredResult<>(1000L*60);
        deferredResult.onTimeout(
                () -> deferredResult.setErrorResult(ResponseEntity.status(HttpStatus.NO_CONTENT)
                        .body(ResponseDTO.ok())));

        deferredResult.onCompletion(
                () -> alarmMap.remove(memberId)
        );

        alarmMap.put(memberId, deferredResult);

        return deferredResult;
    }

    @Async
//    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @EventListener
    public void commentEventHandler(CommentEvent event) {
        Member member = event.member();
        DeferredResult<ResponseEntity<ResponseDTO<AlarmResponseDto>>> deferredResult = alarmMap.get(member.getId());

        //알림 저장 로직
        Alarm alarm = Alarm.builder()
                .targetId(event.commentId())
                .alarmType(AlarmType.SAVED)
                .member(member)
                .checked(false)
                .build();

        Alarm savedAlarm = alarmRepository.save(alarm);

        if (Optional.ofNullable(deferredResult).isPresent()) {
            AlarmResponseDto response = AlarmResponseDto.builder()
                    .alarmId(savedAlarm.getId()) //저장된 알림의 id
                    .targetId(event.commentId())
                    .alarmType(AlarmType.ANSWER)
                    .checked(savedAlarm.getChecked())
                    .build();

            deferredResult.setResult(ResponseEntity
                    .ok(ResponseDTO.okWithData(response)));
            log.info("알림 보냅니다~");
        }
    }

    @Async
//    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @EventListener
    public void bookMarkEventHandler(SavedArticleCommentEvent event) {

        event.members()
                .forEach(member -> {
                    DeferredResult<ResponseEntity<ResponseDTO<AlarmResponseDto>>> deferredResult = alarmMap.get(member.getId());

                    // alarm 저장 로직
                    Alarm alarm = Alarm.builder()
                            .targetId(event.articleId())
                            .alarmType(AlarmType.SAVED)
                            .member(member)
                            .checked(false)
                            .build();

                    Alarm savedAlarm = alarmRepository.save(alarm);


                    if (Optional.ofNullable(deferredResult).isPresent()) {
                        AlarmResponseDto response = AlarmResponseDto.builder()
                                .alarmId(savedAlarm.getId()) //저장된 알림의 id
                                .targetId(event.articleId())
                                .alarmType(AlarmType.SAVED)
                                .checked(savedAlarm.getChecked())
                                .build();

                        deferredResult.setResult(
                                ResponseEntity.ok(
                                        ResponseDTO.okWithData(response)
                                )
                        );
                    }
                    log.info("알림 보냅니다~");
                });
    }

    @Transactional(readOnly = true)
    public List<AlarmResponseDto> getAlarmHistory(UserIdAndRole userIdAndRole) {

        //멤버 검증 로직
        Member foundMember = memberRepository.findById(userIdAndRole.id())
                .orElseThrow(UserNotExistException::new);

        return alarmRepository.findByMember(foundMember).stream()
                .map(alarm -> AlarmResponseDto.builder()
                        .alarmId(alarm.getId())
                        .alarmType(alarm.getAlarmType())
                        .targetId(alarm.getTargetId())
                        .checked(alarm.getChecked())
                        .build())
                .collect(Collectors.toList());
    }

    public void checkedAlarm(Long alarmId, UserIdAndRole userIdAndRole) {

        memberRepository.findById(userIdAndRole.id())
                .orElseThrow(UserNotExistException::new);

        Alarm alarm = alarmRepository.findById(alarmId)
                .orElseThrow(AlarmNotFoundException::new);

        alarm.setChecked(true);
    }
}


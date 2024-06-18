package com.final_10aeat.domain.alarm.controller;

import com.final_10aeat.common.dto.UserIdAndRole;
import com.final_10aeat.common.service.AuthUserService;
import com.final_10aeat.domain.alarm.dto.response.AlarmResponseDto;
import com.final_10aeat.domain.alarm.service.AlarmService;
import com.final_10aeat.global.security.principal.MemberPrincipal;
import com.final_10aeat.global.util.ResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/alarm")
public class AlarmController {

    private final AlarmService alarmService;
    private final AuthUserService authUserService;

    @GetMapping
    public DeferredResult<ResponseEntity<ResponseDTO<AlarmResponseDto>>> getAlarm() {

        MemberPrincipal memberPrincipal = (MemberPrincipal) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();

        return alarmService.getDeferredResult(memberPrincipal.getMember().getId());
    }

    @GetMapping("/history")
    public ResponseDTO<List<AlarmResponseDto>> getAlarmHistory() {

        UserIdAndRole userIdAndRole = authUserService.getCurrentUserIdAndRole();

        return ResponseDTO.okWithData(alarmService.getAlarmHistory(userIdAndRole));
    }

    @PostMapping("/{alarm_id}")
    public ResponseDTO<Void> checkedAlarm(
            @PathVariable("alarm_id") Long alarmId
    ) {
        UserIdAndRole userIdAndRole = authUserService.getCurrentUserIdAndRole();

        alarmService.checkedAlarm(alarmId, userIdAndRole);
        return ResponseDTO.ok();
    }

}

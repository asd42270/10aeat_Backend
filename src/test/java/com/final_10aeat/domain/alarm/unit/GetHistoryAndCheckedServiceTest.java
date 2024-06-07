package com.final_10aeat.domain.alarm.unit;

import com.final_10aeat.common.dto.UserIdAndRole;
import com.final_10aeat.common.enumclass.MemberRole;
import com.final_10aeat.domain.alarm.entity.Alarm;
import com.final_10aeat.domain.alarm.entity.enumtype.AlarmType;
import com.final_10aeat.domain.alarm.exception.AlarmNotFoundException;
import com.final_10aeat.domain.alarm.repository.AlarmRepository;
import com.final_10aeat.domain.alarm.service.AlarmService;
import com.final_10aeat.domain.member.entity.Member;
import com.final_10aeat.domain.member.exception.UserNotExistException;
import com.final_10aeat.domain.member.repository.MemberRepository;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.BDDMockito.given;

public class GetHistoryAndCheckedServiceTest {

    @Mock
    private MemberRepository memberRepository;
    @Mock
    private AlarmRepository alarmRepository;

    @InjectMocks
    private AlarmService alarmService;

    private final Member member = Member.builder()
            .id(1L)
            .email("test@test.com")
            .password("encodedPassword")
            .name("spring")
            .role(MemberRole.OWNER)
            .build();

    private final UserIdAndRole userIdAndRole = new UserIdAndRole(member.getId(), false);

    private final Alarm alarm = Alarm.builder()
            .id(1L)
            .checked(false)
            .alarmType(AlarmType.ANSWER)
            .targetId(1L)
            .member(member)
            .build();

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @DisplayName("getAlarmHistory()는")
    @Nested
    class Context_getAlarmHistory {

        @DisplayName("히스토리를 가져오는데 성공한다.")
        @Test
        void _willSuccess() {
            // given
            given(memberRepository.findById(userIdAndRole.id())).willReturn(Optional.ofNullable(member));
            given(alarmRepository.findByMember(member)).willReturn(Collections.singletonList(alarm));

            // when&then
            assertThat(alarmService.getAlarmHistory(userIdAndRole).size()).isEqualTo(1);
        }

        @DisplayName("사용자가 존재하지 않아 실패한다.")
        @Test
        void _memberNotExist() {
            // given
            given(memberRepository.findById(userIdAndRole.id())).willReturn(Optional.empty());

            // when&then
            assertThrows(UserNotExistException.class, ()-> alarmService.getAlarmHistory(userIdAndRole));
        }
    }

    @Nested
    @DisplayName("checkedAlarm()은")
    class Context_checkedAlarm {

        @DisplayName("확인 여부 수정에 성공한다.")
        @Test
        void _willSuccess() {
            // given
            given(memberRepository.findById(userIdAndRole.id())).willReturn(Optional.ofNullable(member));
            given(alarmRepository.findById(alarm.getId())).willReturn(Optional.of(alarm));

            // when
            alarmService.checkedAlarm(alarm.getId(), userIdAndRole);

            // then
            assertTrue(alarm.getChecked());
        }

        @DisplayName("사용자가 존재하지 않아 실패한다.")
        @Test
        void _memberNotExist() {
            // given
            given(memberRepository.findById(userIdAndRole.id())).willReturn(Optional.empty());

            // when&then
            assertThrows(UserNotExistException.class, ()-> alarmService.checkedAlarm(alarm.getId(), userIdAndRole));
        }

        @DisplayName("알람이 존재하지 않아 실패한다.")
        @Test
        void _alarmNotExist() {
            // given
            given(memberRepository.findById(userIdAndRole.id())).willReturn(Optional.ofNullable(member));
            given(alarmRepository.findById(alarm.getId())).willReturn(Optional.empty());

            // when&then
            assertThrows(AlarmNotFoundException.class, ()-> alarmService.checkedAlarm(alarm.getId(), userIdAndRole));
        }
    }
}

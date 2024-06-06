package com.final_10aeat.domain.alarm.repository;

import com.final_10aeat.domain.alarm.entity.Alarm;
import com.final_10aeat.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AlarmRepository extends JpaRepository<Alarm, Long> {

    List<Alarm> findByMember(Member member);
}

package com.final_10aeat.domain.alarm.entity;

import com.final_10aeat.domain.alarm.entity.enumtype.AlarmType;
import com.final_10aeat.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Alarm {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private AlarmType alarmType;

    @Column(nullable = false)
    private Long targetId;

    @Column(nullable = false)
    @Setter
    private Boolean checked=false;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;


}

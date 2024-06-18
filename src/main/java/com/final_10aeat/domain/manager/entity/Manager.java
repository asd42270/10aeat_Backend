package com.final_10aeat.domain.manager.entity;

import com.final_10aeat.common.enumclass.MemberRole;
import com.final_10aeat.domain.manager.dto.response.GetManagerInfoResponseDto;
import com.final_10aeat.domain.office.entity.Office;
import com.final_10aeat.global.entity.SoftDeletableBaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@Table(name = "manager")
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Manager extends SoftDeletableBaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String email;

    @Column
    private String password;

    @Column
    private String name;

    @Column(name = "phonenumber")
    private String phoneNumber;

    @Column(name = "lunch_start")
    private LocalDateTime lunchBreakStart;

    @Column(name = "lunch_end")
    private LocalDateTime lunchBreakEnd;

    @Column(name = "manager_office")
    private String managerOffice;

    private String affiliation;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MemberRole role = MemberRole.MANAGER;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "office_id")
    private Office office;

    public GetManagerInfoResponseDto toDto() {
        return GetManagerInfoResponseDto.builder()
            .email(this.email)
            .name(this.name)
            .phoneNumber(this.phoneNumber)
            .lunchBreakStart(this.lunchBreakStart)
            .lunchBreakEnd(this.lunchBreakEnd)
            .managerOffice(this.managerOffice)
            .affiliation(this.affiliation)
            .build();
    }
}

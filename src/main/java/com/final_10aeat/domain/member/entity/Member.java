package com.final_10aeat.domain.member.entity;

import com.final_10aeat.common.enumclass.MemberRole;
import com.final_10aeat.domain.office.entity.Office;
import com.final_10aeat.global.entity.SoftDeletableBaseTimeEntity;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;
import lombok.*;

@Entity
@Getter
@Builder
@Table(name = "member", indexes = @Index(
    name = "idx_email_deletedAt", columnList = "email, deletedAt", unique = true
))
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends SoftDeletableBaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String email;

    @Column
    private String password;

    @Column
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MemberRole role;

    @Column(name = "term_agreed")
    private Boolean termAgreed;

    @ManyToMany
    @JoinTable(
        name = "member_building",
        joinColumns = @JoinColumn(name = "member_id"),
        inverseJoinColumns = @JoinColumn(name = "building_info_id")
    )
    private Set<BuildingInfo> buildingInfos;

    @ManyToMany
    @JoinTable(
        name = "member_office",
        joinColumns = @JoinColumn(name = "member_id"),
        inverseJoinColumns = @JoinColumn(name = "office_id")
    )
    private Set<Office> offices;

    @Column(name = "default_office_id")
    private Long defaultOffice;

    public void delete(LocalDateTime currentTime) {
        super.delete(currentTime);
    }
}

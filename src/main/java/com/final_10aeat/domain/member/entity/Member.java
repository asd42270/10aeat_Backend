package com.final_10aeat.domain.member.entity;

import com.final_10aeat.common.enumclass.MemberRole;
import com.final_10aeat.domain.office.entity.Office;
import com.final_10aeat.global.entity.SoftDeletableBaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    @Setter
    @JoinTable(
        name = "member_building",
        joinColumns = @JoinColumn(name = "member_id"),
        inverseJoinColumns = @JoinColumn(name = "building_info_id")
    )
    private Set<BuildingInfo> buildingInfos = new HashSet<>();

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

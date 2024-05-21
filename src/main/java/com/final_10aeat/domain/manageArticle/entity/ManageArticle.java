package com.final_10aeat.domain.manageArticle.entity;

import com.final_10aeat.common.enumclass.ManagePeriod;
import com.final_10aeat.common.enumclass.Progress;
import com.final_10aeat.global.entity.SoftDeletableBaseTimeEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@Table(name = "manage_article")
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ManageArticle extends SoftDeletableBaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Progress progress = Progress.INPROGRESS;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ManagePeriod period;

    @Column(name = "period_count")
    private int periodCount;

    @Column
    private String title;

    @Column(name = "legal_basis")
    private String legalBasis;

    @Column
    private String target;

    @Column
    private String manager;

    @Column(columnDefinition = "TEXT")
    private String note;

    @OneToMany(mappedBy = "manageArticle", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ManageSchedule> schedules;
}

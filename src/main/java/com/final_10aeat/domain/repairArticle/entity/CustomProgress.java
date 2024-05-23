package com.final_10aeat.domain.repairArticle.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
import lombok.Setter;

@Entity
@Getter
@Builder
@Table(name = "repair_custom_progress")
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CustomProgress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    @Setter
    private String title;

    @Column
    @Setter
    private String content;

    @Column(name = "inprogress")
    @Setter
    private boolean inProgress;

    @Column(name = "schedule_start")
    @Setter
    private LocalDateTime startSchedule;

    @Column(name = "schedule_end")
    @Setter
    private LocalDateTime endSchedule;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "repair_article_id")
    private RepairArticle repairArticle;
}

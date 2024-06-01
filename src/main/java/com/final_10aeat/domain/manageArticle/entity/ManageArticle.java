package com.final_10aeat.domain.manageArticle.entity;

import com.final_10aeat.common.enumclass.ManagePeriod;
import com.final_10aeat.common.enumclass.Progress;
import com.final_10aeat.domain.office.entity.Office;
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
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    @Setter
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Progress progress;

    @Setter
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ManagePeriod period;

    @Setter
    @Column(name = "period_count")
    private Integer periodCount;

    @Setter
    @Column(nullable = false)
    private String title;

    @Setter
    @Column(name = "legal_basis")
    private String legalBasis;

    @Setter
    @Column
    private String target;

    @Setter
    @Column
    private String responsibility;

    @Setter
    @Column(columnDefinition = "TEXT")
    private String note;

    @OneToMany(mappedBy = "manageArticle", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ManageSchedule> schedules;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "office_id")
    private Office office;

    public void delete(LocalDateTime currentTime) {
        super.delete(currentTime);
    }

    public void addSchedules(List<ManageSchedule> newSchedules) {
        schedules.addAll(newSchedules);
    }

    public void addSchedule(ManageSchedule newSchedule) {
        schedules.add(newSchedule);
    }

    public void deleteSchedule(ManageSchedule manageSchedule) {
        schedules.remove(manageSchedule);
    }
}

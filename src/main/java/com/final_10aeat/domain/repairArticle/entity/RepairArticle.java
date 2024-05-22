package com.final_10aeat.domain.repairArticle.entity;

import com.final_10aeat.common.enumclass.ArticleCategory;
import com.final_10aeat.common.enumclass.Progress;
import com.final_10aeat.domain.manager.entity.Manager;
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
import java.util.Set;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@Table(name = "repair_article")
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RepairArticle extends SoftDeletableBaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Progress progress = Progress.INPROGRESS;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ArticleCategory category;

    @Column
    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Column(name = "construction_start")
    private LocalDateTime startConstruction;

    @Column(name = "construction_end")
    private LocalDateTime endConstruction;

    @Column(name = "repair_company")
    private String company;

    @Column(name = "repair_company_website", columnDefinition = "TEXT")
    private String companyWebsite;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manager_id")
    private Manager manager;

    @OneToMany(mappedBy = "repairArticle", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<RepairArticleImage> images;
}

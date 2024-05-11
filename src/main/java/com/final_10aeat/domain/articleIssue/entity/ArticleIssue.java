package com.final_10aeat.domain.articleIssue.entity;

import com.final_10aeat.domain.manageArticle.entity.ManageArticle;
import com.final_10aeat.domain.repairArticle.entity.RepairArticle;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@Table(name = "article_issue")
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ArticleIssue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manage_article_id", referencedColumnName = "id")
    private ManageArticle manageArticle;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "repair_article_id", referencedColumnName = "id")
    private RepairArticle repairArticle;
}

package com.final_10aeat.domain.articleIssue.entity;

import com.final_10aeat.domain.manageArticle.entity.ManageArticle;
import com.final_10aeat.domain.repairArticle.entity.RepairArticle;
import com.final_10aeat.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@Table(name = "article_issue")
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ArticleIssue extends BaseTimeEntity {

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

    public ArticleIssue(String title, String content, ManageArticle manageArticle, LocalDateTime createdAt) {
        this.title = title;
        this.content = content;
        this.manageArticle = manageArticle;
        this.createdAt = createdAt;
    }

    public ArticleIssue(String title, String content, RepairArticle repairArticle, LocalDateTime createdAt) {
        this.title = title;
        this.content = content;
        this.repairArticle = repairArticle;
        this.createdAt = createdAt;
    }
}

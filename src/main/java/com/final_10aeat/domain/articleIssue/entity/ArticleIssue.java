package com.final_10aeat.domain.articleIssue.entity;

import com.final_10aeat.domain.manageArticle.entity.ManageArticle;
import com.final_10aeat.domain.manager.entity.Manager;
import com.final_10aeat.domain.repairArticle.entity.RepairArticle;
import com.final_10aeat.global.entity.SoftDeletableBaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@Table(name = "article_issue")
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ArticleIssue extends SoftDeletableBaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    @Setter
    private String title;

    @Column(columnDefinition = "TEXT")
    @Setter
    private String content;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manage_article_id", referencedColumnName = "id")
    private ManageArticle manageArticle;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "repair_article_id", referencedColumnName = "id")
    private RepairArticle repairArticle;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manager_id")
    private Manager manager;

    public ArticleIssue(String title, String content, ManageArticle manageArticle,
                        LocalDateTime createdAt, Manager manager) {
        this.title = title;
        this.content = content;
        this.manageArticle = manageArticle;
        this.createdAt = createdAt;
        this.manager = manager;
    }

    public ArticleIssue(String title, String content, RepairArticle repairArticle,
                        LocalDateTime createdAt, Manager manager) {
        this.title = title;
        this.content = content;
        this.repairArticle = repairArticle;
        this.createdAt = createdAt;
        this.manager = manager;
    }

    public void delete(LocalDateTime currentTime) {
        super.delete(currentTime);
    }
}

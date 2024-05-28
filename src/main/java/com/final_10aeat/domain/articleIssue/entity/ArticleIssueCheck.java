package com.final_10aeat.domain.articleIssue.entity;

import com.final_10aeat.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@Table(name = "article_issue_ischeck")
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ArticleIssueCheck {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "checked")
    private boolean checked=false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_issue_id")
    private ArticleIssue articleIssue;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;
}

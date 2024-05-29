package com.final_10aeat.domain.articleIssue.service;

import com.final_10aeat.common.exception.ArticleNotFoundException;
import com.final_10aeat.domain.articleIssue.dto.request.ArticleIssueCheckRequestDto;
import com.final_10aeat.domain.articleIssue.dto.response.ArticleIssueCheckResponseDto;
import com.final_10aeat.domain.articleIssue.entity.ArticleIssue;
import com.final_10aeat.domain.articleIssue.entity.ArticleIssueCheck;
import com.final_10aeat.domain.articleIssue.exception.IssueNotFoundException;
import com.final_10aeat.domain.articleIssue.repository.ArticleIssueCheckRepository;
import com.final_10aeat.domain.articleIssue.repository.ArticleIssueRepository;
import com.final_10aeat.domain.manageArticle.entity.ManageArticle;
import com.final_10aeat.domain.manageArticle.repository.ManageArticleRepository;
import com.final_10aeat.domain.member.entity.Member;
import com.final_10aeat.domain.repairArticle.entity.RepairArticle;
import com.final_10aeat.domain.repairArticle.repository.RepairArticleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ArticleIssueCheckService {

    private final ArticleIssueRepository articleIssueRepository;
    private final ArticleIssueCheckRepository articleIssueCheckRepository;
    private final ManageArticleRepository manageArticleRepository;
    private final RepairArticleRepository repairArticleRepository;

    public ArticleIssueCheckResponseDto manageIssueCheck(ArticleIssueCheckRequestDto requestDto,
                                                   Long manageArticleId, Member member) {

        //리팩토링 하면서 redis 캐시 설정하면 캐시에 article 정보 저장해 놨다가 불러오는 방법도 괜찮을 것 같아요!!
        ManageArticle manageArticle = manageArticleRepository.findById(manageArticleId)
                .orElseThrow(ArticleNotFoundException::new);

        ArticleIssue articleIssue = articleIssueRepository.findFirstByManageArticleOrderByCreatedAtDesc(manageArticle)
                .orElseThrow(IssueNotFoundException::new);

        return getArticleIssueCheckResponseDto(requestDto, member, articleIssue);
    }

    public ArticleIssueCheckResponseDto repairIssueCheck(ArticleIssueCheckRequestDto requestDto,
                                                         Long repairArticleId, Member member) {

        //리팩토링 하면서 redis 캐시 설정하면 캐시에 article 정보 저장해 놨다가 불러오는 방법도 괜찮을 것 같아요!!
        RepairArticle repairArticle = repairArticleRepository.findById(repairArticleId)
                .orElseThrow(ArticleNotFoundException::new);

        ArticleIssue articleIssue = articleIssueRepository.findFirstByRepairArticleOrderByCreatedAtDesc(repairArticle)
                .orElseThrow(IssueNotFoundException::new);

        return getArticleIssueCheckResponseDto(requestDto, member, articleIssue);
    }

    private ArticleIssueCheckResponseDto getArticleIssueCheckResponseDto(ArticleIssueCheckRequestDto requestDto, Member member, ArticleIssue articleIssue) {

        ArticleIssueCheck issueCheck = ArticleIssueCheck.builder()
                .articleIssue(articleIssue)
                .checked(requestDto.check())
                .member(member)
                .build();

        articleIssueCheckRepository.save(issueCheck);

        return ArticleIssueCheckResponseDto.builder()
                .title(articleIssue.getTitle())
                .content(articleIssue.getContent())
                .check(issueCheck.isChecked())
                .build();
    }
}

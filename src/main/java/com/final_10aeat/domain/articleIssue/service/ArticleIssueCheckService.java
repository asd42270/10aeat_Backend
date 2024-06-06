package com.final_10aeat.domain.articleIssue.service;

import com.final_10aeat.domain.articleIssue.dto.request.ArticleIssueCheckRequestDto;
import com.final_10aeat.domain.articleIssue.dto.response.ArticleIssueCheckResponseDto;
import com.final_10aeat.domain.articleIssue.entity.ArticleIssue;
import com.final_10aeat.domain.articleIssue.entity.ArticleIssueCheck;
import com.final_10aeat.domain.articleIssue.exception.InactiveIssueException;
import com.final_10aeat.domain.articleIssue.exception.IssueNotFoundException;
import com.final_10aeat.domain.articleIssue.repository.ArticleIssueCheckRepository;
import com.final_10aeat.domain.articleIssue.repository.ArticleIssueRepository;
import com.final_10aeat.domain.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ArticleIssueCheckService {

    private final ArticleIssueRepository articleIssueRepository;
    private final ArticleIssueCheckRepository articleIssueCheckRepository;

    public void checkIssue(ArticleIssueCheckRequestDto requestDto, Long issueId, Member member) {
        ArticleIssue articleIssue = articleIssueRepository.findById(issueId)
            .orElseThrow(IssueNotFoundException::new);

        if (!articleIssue.isActive()) {
            throw new InactiveIssueException();
        }

        ArticleIssueCheck issueCheck = ArticleIssueCheck.builder()
            .articleIssue(articleIssue)
            .checked(requestDto.check())
            .member(member)
            .build();

        articleIssueCheckRepository.save(issueCheck);
    }

    public ArticleIssueCheckResponseDto getIssueDetail(Long issueId) {
        ArticleIssue articleIssue = articleIssueRepository.findById(issueId)
            .orElseThrow(IssueNotFoundException::new);

        if (!articleIssue.isActive()) {
            throw new InactiveIssueException();
        }

        return ArticleIssueCheckResponseDto.builder()
            .title(articleIssue.getTitle())
            .content(articleIssue.getContent())
            .build();
    }
}

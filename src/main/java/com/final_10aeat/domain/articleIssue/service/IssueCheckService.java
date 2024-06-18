package com.final_10aeat.domain.articleIssue.service;

import com.final_10aeat.domain.articleIssue.dto.request.CheckIssueRequestDto;
import com.final_10aeat.domain.articleIssue.dto.response.IssueDetailResponseDto;
import com.final_10aeat.domain.articleIssue.entity.ArticleIssue;
import com.final_10aeat.domain.articleIssue.entity.ArticleIssueCheck;
import com.final_10aeat.domain.articleIssue.exception.DisabledIssueException;
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
public class IssueCheckService {

    private final ArticleIssueRepository articleIssueRepository;
    private final ArticleIssueCheckRepository articleIssueCheckRepository;

    public void checkIssue(CheckIssueRequestDto requestDto, Long issueId, Member member) {
        ArticleIssue articleIssue = articleIssueRepository.findById(issueId)
            .orElseThrow(IssueNotFoundException::new);

        if (!articleIssue.isEnabled()) {
            throw new DisabledIssueException();
        }

        ArticleIssueCheck issueCheck = ArticleIssueCheck.builder()
            .articleIssue(articleIssue)
            .checked(requestDto.check())
            .member(member)
            .build();

        articleIssueCheckRepository.save(issueCheck);
    }

    public IssueDetailResponseDto getIssueDetail(Long issueId) {
        ArticleIssue articleIssue = articleIssueRepository.findById(issueId)
            .orElseThrow(IssueNotFoundException::new);

        if (!articleIssue.isEnabled()) {
            throw new DisabledIssueException();
        }

        return IssueDetailResponseDto.builder()
            .title(articleIssue.getTitle())
            .content(articleIssue.getContent())
            .build();
    }
}

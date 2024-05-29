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
import org.springframework.data.redis.core.RedisTemplate;
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
    private final RedisTemplate<String, Object> redisTemplate;

    public ArticleIssueCheckResponseDto manageIssueCheck(ArticleIssueCheckRequestDto requestDto, Long manageArticleId, Member member) {

        //리팩토링 하면서 redis 캐시 설정하면 캐시에 article 정보 저장해 놨다가 불러오는 방법도 괜찮을 것 같아요!!
        ManageArticle manageArticle = manageArticleRepository.findById(manageArticleId)
                .orElseThrow(ArticleNotFoundException::new);

        ArticleIssue articleIssue = articleIssueRepository.findFirstByManageArticleOrderByCreatedAtDesc(manageArticle)
                .orElseThrow(IssueNotFoundException::new);

        ArticleIssueCheckResponseDto responseDto = getArticleIssueCheckResponseDto(requestDto, member,
                articleIssue, manageArticleId);

        saveManageIssueToRedis(responseDto, manageArticleId);

        return responseDto;
    }

    public ArticleIssueCheckResponseDto repairIssueCheck(ArticleIssueCheckRequestDto requestDto,
                                                         Long repairArticleId, Member member) {

        //리팩토링 하면서 redis 캐시 설정하면 캐시에 article 정보 저장해 놨다가 불러오는 방법도 괜찮을 것 같아요!!
        RepairArticle repairArticle = repairArticleRepository.findById(repairArticleId)
                .orElseThrow(ArticleNotFoundException::new);

        ArticleIssue articleIssue = articleIssueRepository.findFirstByRepairArticleOrderByCreatedAtDesc(repairArticle)
                .orElseThrow(IssueNotFoundException::new);

        ArticleIssueCheckResponseDto responseDto = getArticleIssueCheckResponseDto(requestDto, member,
                articleIssue, repairArticleId);

        saveRepairIssueToRedis(responseDto, repairArticleId);

        return responseDto;
    }

    public ArticleIssueCheckResponseDto getRepairIssueDetail(Long articleId) {

        String key = "repair article" + articleId;

        Object responseDto = redisTemplate.opsForValue().get(key);

        if(responseDto==null) {
            throw new IssueNotFoundException();
        }

        return (ArticleIssueCheckResponseDto) responseDto;
    }

    public ArticleIssueCheckResponseDto getManageIssueDetail(Long articleId) {

        String key = "manage article" + articleId;

        Object responseDto = redisTemplate.opsForValue().get(key);

        if(responseDto==null) {
            throw new IssueNotFoundException();
        }

        return (ArticleIssueCheckResponseDto) responseDto;
    }

    private ArticleIssueCheckResponseDto getArticleIssueCheckResponseDto(ArticleIssueCheckRequestDto requestDto, Member member,
                                                                         ArticleIssue articleIssue, Long articleId) {

        ArticleIssueCheck issueCheck = ArticleIssueCheck.builder()
                .articleIssue(articleIssue)
                .checked(requestDto.check())
                .member(member)
                .build();

        articleIssueCheckRepository.save(issueCheck);

        ArticleIssueCheckResponseDto responseDto = ArticleIssueCheckResponseDto.builder()
                .id(articleIssue.getId())
                .title(articleIssue.getTitle())
                .content(articleIssue.getContent())
                .check(issueCheck.isChecked())
                .build();;

        return responseDto;
    }

    private void saveRepairIssueToRedis(ArticleIssueCheckResponseDto responseDto, Long articleId) {

        String key = "repair article" + articleId;

        redisTemplate.opsForValue().set(key, responseDto);
    }

    private void saveManageIssueToRedis(ArticleIssueCheckResponseDto responseDto, Long articleId) {

        String key = "manage article" + articleId;

        redisTemplate.opsForValue().set(key, responseDto);
    }

}

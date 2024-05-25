package com.final_10aeat.domain.manageArticle.service;

import com.final_10aeat.common.enumclass.Progress;
import com.final_10aeat.common.exception.UnauthorizedAccessException;
import com.final_10aeat.domain.manageArticle.dto.request.CreateManageArticleRequestDto;
import com.final_10aeat.domain.manageArticle.dto.request.UpdateManageArticleRequestDto;
import com.final_10aeat.domain.manageArticle.entity.ManageArticle;
import com.final_10aeat.domain.manageArticle.repository.ManageArticleRepository;
import com.final_10aeat.domain.manager.entity.Manager;
import com.final_10aeat.domain.office.entity.Office;
import com.final_10aeat.domain.repairArticle.exception.ArticleAlreadyDeletedException;
import com.final_10aeat.domain.repairArticle.exception.ArticleNotFoundException;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ManagerManageArticleService {

    private final ManageArticleRepository manageArticleRepository;


    public void create(CreateManageArticleRequestDto request, Manager manager) {
        ManageArticle article = createArticle(request, manager.getOffice());

        manageArticleRepository.save(article);
    }

    public void update(
        Long manageArticleId,
        UpdateManageArticleRequestDto request,
        Manager manager
    ) {
        ManageArticle article = manageArticleRepository.findById(manageArticleId)
            .orElseThrow(ArticleNotFoundException::new);

        if(article.isDeleted()){
            throw new ArticleAlreadyDeletedException();
        }

        if (!article.getOffice().getId().equals(manager.getOffice().getId())) {
            throw new UnauthorizedAccessException();
        }

        updateArticle(request, article);
    }

    private void updateArticle(UpdateManageArticleRequestDto request, ManageArticle article) {
        if (request.period() != null) {
            article.setPeriod(request.period());
        }
        if (request.periodCount() != null) {
            article.setPeriodCount(request.periodCount());
        }
        if (request.title() != null) {
            article.setTitle(request.title());
        }
        if (request.legalBasis() != null) {
            article.setLegalBasis(request.legalBasis());
        }
        if (request.target() != null) {
            article.setTarget(request.target());
        }
        if (request.responsibility() != null) {
            article.setResponsibility(request.responsibility());
        }
        if (request.note() != null) {
            article.setNote(request.note());
        }
    }

    private ManageArticle createArticle(CreateManageArticleRequestDto request, Office office) {
        return ManageArticle.builder()
            .progress(Progress.PENDING)
            .period(request.period())
            .periodCount(request.periodCount())
            .title(request.title())
            .legalBasis(request.legalBasis())
            .target(request.target())
            .responsibility(request.responsibility())
            .note(request.note())
            .office(office)
            .build();
    }

    public void delete(Long manageArticleId, Manager manager) {
        ManageArticle article = manageArticleRepository.findById(manageArticleId)
            .orElseThrow(ArticleNotFoundException::new);

        if(article.isDeleted()){
            throw new ArticleAlreadyDeletedException();
        }

        if (!article.getOffice().getId().equals(manager.getOffice().getId())) {
            throw new UnauthorizedAccessException();
        }

        article.delete(LocalDateTime.now());
    }
}

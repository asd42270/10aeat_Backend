package com.final_10aeat.domain.manageArticle.service;

import com.final_10aeat.common.exception.UnauthorizedAccessException;
import com.final_10aeat.domain.manageArticle.dto.request.ScheduleRequestDto;
import com.final_10aeat.domain.manageArticle.dto.request.util.ScheduleConverter;
import com.final_10aeat.domain.manageArticle.entity.ManageArticle;
import com.final_10aeat.domain.manageArticle.entity.ManageSchedule;
import com.final_10aeat.domain.manageArticle.exception.ScheduleNotFoundException;
import com.final_10aeat.domain.manageArticle.repository.ManageArticleRepository;
import com.final_10aeat.domain.manageArticle.repository.ManageScheduleRepository;
import com.final_10aeat.domain.manager.entity.Manager;
import com.final_10aeat.domain.repairArticle.exception.ArticleAlreadyDeletedException;
import com.final_10aeat.domain.repairArticle.exception.ArticleNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ManageScheduleService {

    private final ManageArticleRepository manageArticleRepository;
    private final ManageScheduleRepository manageScheduleRepository;

    public void register(Long manageArticleId, ScheduleRequestDto request, Manager manager) {
        ManageArticle article = manageArticleRepository.findById(manageArticleId)
            .orElseThrow(ArticleNotFoundException::new);

        if (article.isDeleted()) {
            throw new ArticleAlreadyDeletedException();
        }

        if (!article.getOffice().getId().equals(manager.getOffice().getId())) {
            throw new UnauthorizedAccessException();
        }

        article.addSchedule(
            ScheduleConverter.toSchedule(request)
        );
    }

    public void complete(Long manageArticleId, Long manageScheduleId, Manager manager) {
        ManageArticle article = manageArticleRepository.findById(manageArticleId)
            .orElseThrow(ArticleNotFoundException::new);

        if (article.isDeleted()) {
            throw new ArticleAlreadyDeletedException();
        }

        if (!article.getOffice().getId().equals(manager.getOffice().getId())) {
            throw new UnauthorizedAccessException();
        }

        ManageSchedule manageSchedule = manageScheduleRepository.findAndPessimisticLockById(manageScheduleId)
            .orElseThrow(ScheduleNotFoundException::new);

        manageSchedule.complete();
    }
}

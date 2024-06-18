package com.final_10aeat.domain.manageArticle.service;

import static java.util.Optional.ofNullable;

import com.final_10aeat.common.enumclass.Progress;
import com.final_10aeat.common.exception.UnauthorizedAccessException;
import com.final_10aeat.domain.manageArticle.dto.request.CreateManageArticleRequestDto;
import com.final_10aeat.domain.manageArticle.dto.request.UpdateManageArticleRequestDto;
import com.final_10aeat.domain.manageArticle.dto.util.ScheduleConverter;
import com.final_10aeat.domain.manageArticle.entity.ManageArticle;
import com.final_10aeat.domain.manageArticle.entity.ManageSchedule;
import com.final_10aeat.domain.manageArticle.repository.ManageArticleRepository;
import com.final_10aeat.domain.manager.entity.Manager;
import com.final_10aeat.domain.office.entity.Office;
import com.final_10aeat.common.exception.ArticleAlreadyDeletedException;
import com.final_10aeat.domain.repairArticle.exception.ArticleNotFoundException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
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

        List<ManageSchedule> scheduleList = request.schedule().stream()
            .map(requestDto -> ScheduleConverter.toSchedule(requestDto, article)).toList();

        article.addSchedules(scheduleList);

        manageArticleRepository.save(article);
    }

    public void update(
        Long manageArticleId,
        UpdateManageArticleRequestDto request,
        Manager manager
    ) {
        ManageArticle article = manageArticleRepository.findById(manageArticleId)
            .orElseThrow(ArticleNotFoundException::new);

        if (article.isDeleted()) {
            throw new ArticleAlreadyDeletedException();
        }

        if (!article.getOffice().getId().equals(manager.getOffice().getId())) {
            throw new UnauthorizedAccessException();
        }

        updateArticle(request, article);
    }

    private void updateArticle(UpdateManageArticleRequestDto request, ManageArticle article) {
        ofNullable(request.period()).ifPresent(article::setPeriod);
        ofNullable(request.periodCount()).ifPresent(article::setPeriodCount);
        ofNullable(request.title()).ifPresent(article::setTitle);
        ofNullable(request.legalBasis()).ifPresent(article::setLegalBasis);
        ofNullable(request.target()).ifPresent(article::setTarget);
        ofNullable(request.responsibility()).ifPresent(article::setResponsibility);
        ofNullable(request.note()).ifPresent(article::setNote);
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
            .schedules(new ArrayList<>())
            .build();
    }

    public void delete(Long manageArticleId, Manager manager) {
        ManageArticle article = manageArticleRepository.findById(manageArticleId)
            .orElseThrow(ArticleNotFoundException::new);

        if (article.isDeleted()) {
            throw new ArticleAlreadyDeletedException();
        }

        if (!article.getOffice().getId().equals(manager.getOffice().getId())) {
            throw new UnauthorizedAccessException();
        }

        article.delete(LocalDateTime.now());
    }
}

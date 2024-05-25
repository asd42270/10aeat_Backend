package com.final_10aeat.domain.manageArticle.service;

import com.final_10aeat.common.enumclass.Progress;
import com.final_10aeat.domain.manageArticle.dto.request.CreateManageArticleRequestDto;
import com.final_10aeat.domain.manageArticle.entity.ManageArticle;
import com.final_10aeat.domain.manageArticle.repository.ManageArticleRepository;
import com.final_10aeat.domain.manager.entity.Manager;
import com.final_10aeat.domain.office.entity.Office;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ManagerManageArticleService {

    private final ManageArticleRepository manageArticleRepository;


    public void create(CreateManageArticleRequestDto request, Manager manager) {
        Office office = manager.getOffice();

        ManageArticle article = createArticle(request, office);

        manageArticleRepository.save(article);
    }

    private ManageArticle createArticle(CreateManageArticleRequestDto request, Office office) {
        return ManageArticle.builder()
            .progress(Progress.INPROGRESS)
            .period(request.period())
            .periodCount(request.periodCount())
            .target(request.title())
            .responsibility(request.responsibility())
            .note(request.note())
            .office(office)
            .build();
    }
}

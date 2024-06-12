package com.final_10aeat.domain.repairArticle.service;

import com.final_10aeat.common.exception.UnauthorizedAccessException;
import com.final_10aeat.domain.manager.entity.Manager;
import com.final_10aeat.domain.manager.repository.ManagerRepository;
import com.final_10aeat.domain.repairArticle.dto.request.CreateCustomProgressRequestDto;
import com.final_10aeat.domain.repairArticle.dto.request.CreateRepairArticleRequestDto;
import com.final_10aeat.domain.repairArticle.dto.request.UpdateCustomProgressRequestDto;
import com.final_10aeat.domain.repairArticle.dto.request.UpdateRepairArticleRequestDto;
import com.final_10aeat.domain.repairArticle.entity.CustomProgress;
import com.final_10aeat.domain.repairArticle.entity.RepairArticle;
import com.final_10aeat.domain.repairArticle.entity.RepairArticleImage;
import com.final_10aeat.domain.repairArticle.exception.ArticleAlreadyDeletedException;
import com.final_10aeat.domain.repairArticle.exception.ArticleNotFoundException;
import com.final_10aeat.domain.repairArticle.exception.CustomProgressNotFoundException;
import com.final_10aeat.domain.repairArticle.exception.ImageNotFoundException;
import com.final_10aeat.domain.repairArticle.exception.ManagerNotFoundException;
import com.final_10aeat.domain.repairArticle.repository.CustomProgressRepository;
import com.final_10aeat.domain.repairArticle.repository.RepairArticleImageRepository;
import com.final_10aeat.domain.repairArticle.repository.RepairArticleRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ManagerRepairArticleService {

    private final RepairArticleRepository repairArticleRepository;
    private final ManagerRepository managerRepository;
    private final CustomProgressRepository customProgressRepository;
    private final RepairArticleImageRepository repairArticleImageRepository;

    public void createRepairArticle(CreateRepairArticleRequestDto request,
        Long managerId) {
        Manager manager = managerRepository.findById(managerId).orElseThrow(
            ManagerNotFoundException::new);
        RepairArticle repairArticle = buildRepairArticleFromRequest(request, manager);
        List<RepairArticleImage> images = createImageEntitiesFromIds(request.imageIds(),
            repairArticle);
        repairArticle.setImages(images);
        repairArticleRepository.save(repairArticle);
    }

    private RepairArticle buildRepairArticleFromRequest(CreateRepairArticleRequestDto request,
        Manager manager) {
        return RepairArticle.builder()
            .category(request.category())
            .progress(request.progress())
            .title(request.title())
            .content(request.content())
            .startConstruction(request.constructionStart())
            .endConstruction(request.constructionEnd())
            .company(request.repairCompany())
            .companyWebsite(request.repairCompanyWebsite())
            .manager(manager)
            .office(manager.getOffice())
            .build();
    }

    private List<RepairArticleImage> createImageEntitiesFromIds(List<Long> imageIds,
        RepairArticle repairArticle) {
        return imageIds.stream()
            .map(id -> repairArticleImageRepository.findById(id).orElseThrow(
                ImageNotFoundException::new))
            .peek(image -> image.setRepairArticle(repairArticle))
            .collect(Collectors.toList());
    }

    public void deleteRepairArticleById(Long repairArticleId, Long managerId) {
        RepairArticle repairArticle = repairArticleRepository.findById(repairArticleId)
            .orElseThrow(ArticleNotFoundException::new);

        verifyManager(repairArticle, managerId);

        repairArticle.delete(LocalDateTime.now());
    }

    public void updateRepairArticle(Long repairArticleId, UpdateRepairArticleRequestDto request,
        Long managerId) {
        RepairArticle repairArticle = repairArticleRepository.findById(repairArticleId)
            .orElseThrow(ArticleNotFoundException::new);

        verifyManager(repairArticle, managerId);
        if (repairArticle.isDeleted()) {
            throw new ArticleAlreadyDeletedException();
        }

        updateFields(request, repairArticle);
        repairArticleRepository.save(repairArticle);
    }

    private void verifyManager(RepairArticle repairArticle, Long managerId) {
        if (!repairArticle.getManager().getId().equals(managerId)) {
            throw new UnauthorizedAccessException();
        }
    }

    private void updateFields(UpdateRepairArticleRequestDto request, RepairArticle repairArticle) {
        Optional.ofNullable(request.category()).ifPresent(repairArticle::setCategory);
        Optional.ofNullable(request.progress()).ifPresent(repairArticle::setProgress);
        Optional.ofNullable(request.title()).ifPresent(repairArticle::setTitle);
        Optional.ofNullable(request.content()).ifPresent(repairArticle::setContent);
        Optional.ofNullable(request.constructionStart())
            .ifPresent(repairArticle::setStartConstruction);
        Optional.ofNullable(request.constructionEnd()).ifPresent(repairArticle::setEndConstruction);
        Optional.ofNullable(request.repairCompany()).ifPresent(repairArticle::setCompany);
        Optional.ofNullable(request.repairCompanyWebsite())
            .ifPresent(repairArticle::setCompanyWebsite);
    }

    public void createCustomProgress(Long repairArticleId, Long managerId,
        CreateCustomProgressRequestDto request) {
        RepairArticle repairArticle = repairArticleRepository.findById(repairArticleId)
            .orElseThrow(ArticleNotFoundException::new);

        if (!repairArticle.getManager().getId().equals(managerId)) {
            throw new UnauthorizedAccessException();
        }

        CustomProgress customProgress = CustomProgress.builder()
            .title(request.title())
            .content(request.content())
            .startSchedule(request.startSchedule())
            .inProgress(false)
            .repairArticle(repairArticle)
            .build();

        customProgressRepository.save(customProgress);
    }

    public void updateCustomProgress(Long progressId, Long managerId,
        UpdateCustomProgressRequestDto request) {
        CustomProgress customProgress = customProgressRepository.findById(progressId)
            .orElseThrow(CustomProgressNotFoundException::new);

        RepairArticle repairArticle = customProgress.getRepairArticle();
        verifyManager(repairArticle, managerId);

        Optional.ofNullable(request.startSchedule()).ifPresent(customProgress::setStartSchedule);
        Optional.ofNullable(request.title()).ifPresent(customProgress::setTitle);
        Optional.ofNullable(request.content()).ifPresent(customProgress::setContent);
        Optional.ofNullable(request.inProgress()).ifPresent(inProgress -> {
            if (inProgress) {
                repairArticle.getCustomProgressSet().forEach(cp -> {
                    if (!cp.equals(customProgress)) {
                        cp.setInProgress(false);
                    }
                });
            }
            customProgress.setInProgress(inProgress);
        });
        customProgressRepository.save(customProgress);
    }
}

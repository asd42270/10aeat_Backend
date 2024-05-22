package com.final_10aeat.domain.repairArticle.service;

import com.final_10aeat.common.exception.UnauthorizedAccessException;
import com.final_10aeat.domain.manager.entity.Manager;
import com.final_10aeat.domain.manager.repository.ManagerRepository;
import com.final_10aeat.domain.repairArticle.dto.request.CreateRepairArticleRequestDto;
import com.final_10aeat.domain.repairArticle.entity.RepairArticle;
import com.final_10aeat.domain.repairArticle.entity.RepairArticleImage;
import com.final_10aeat.domain.repairArticle.exception.ArticleNotFoundException;
import com.final_10aeat.domain.repairArticle.exception.ManagerNotFoundException;
import com.final_10aeat.domain.repairArticle.repository.RepairArticleRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class RepairArticleService {

    private final RepairArticleRepository repairArticleRepository;
    private final ManagerRepository managerRepository;

    public void createRepairArticle(CreateRepairArticleRequestDto request,
        Long managerId) {
        Manager manager = managerRepository.findById(managerId).orElseThrow(
            ManagerNotFoundException::new);
        RepairArticle repairArticle = RepairArticle.builder()
            .category(request.category())
            .progress(request.progress())
            .title(request.title())
            .content(request.content())
            .startConstruction(request.constructionStart())
            .endConstruction(request.constructionEnd())
            .company(request.repairCompany())
            .companyWebsite(request.repairCompanyWebsite())
            .manager(manager)
            .build();
        Set<RepairArticleImage> images = createImageEntities(request.images(), repairArticle);
        repairArticle.setImages(images);

        repairArticleRepository.save(repairArticle);
    }

    private Set<RepairArticleImage> createImageEntities(List<String> imageUrls,
        RepairArticle repairArticle) {
        return imageUrls.stream()
            .map(url -> RepairArticleImage.builder()
                .imageUrl(url)
                .repairArticle(repairArticle)
                .build())
            .collect(Collectors.toSet());
    }

    public void deleteRepairArticleById(Long repairArticleId, Long managerId) {
        RepairArticle repairArticle = repairArticleRepository.findById(repairArticleId)
            .orElseThrow(ArticleNotFoundException::new);

        if (!repairArticle.getManager().getId().equals(managerId)) {
            throw new UnauthorizedAccessException();
        }

        repairArticle.delete(LocalDateTime.now());
        repairArticleRepository.save(repairArticle);
    }
}

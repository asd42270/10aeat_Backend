package com.final_10aeat.domain.repairArticle.service;

import com.final_10aeat.common.dto.UserIdAndRole;
import com.final_10aeat.domain.repairArticle.dto.response.ImageResponseDto;
import com.final_10aeat.domain.repairArticle.entity.RepairArticleImage;
import com.final_10aeat.domain.repairArticle.repository.RepairArticleImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ManagerRepairArticleImageService {

    private final RepairArticleImageRepository repairArticleImageRepository;

    public ImageResponseDto saveImage(String imageUrl, UserIdAndRole userIdAndRole) {

        RepairArticleImage repairArticleImage = RepairArticleImage.builder()
            .imageUrl(imageUrl)
            .build();

        RepairArticleImage savedImage = repairArticleImageRepository.save(repairArticleImage);

        return ImageResponseDto.builder()
            .imageId(savedImage.getId())
            .imageUrl(savedImage.getImageUrl())
            .build();
    }
}

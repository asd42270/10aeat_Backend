package com.final_10aeat.domain.repairArticle.service;

import com.final_10aeat.common.service.UploadImageComponent;
import com.final_10aeat.domain.repairArticle.dto.response.ImageResponseDto;
import com.final_10aeat.domain.repairArticle.entity.RepairArticleImage;
import com.final_10aeat.domain.repairArticle.exception.ImageNotFoundException;
import com.final_10aeat.domain.repairArticle.repository.RepairArticleImageRepository;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Transactional
public class ManagerRepairArticleImageService {

    private final RepairArticleImageRepository repairArticleImageRepository;
    private final UploadImageComponent uploadImageComponent;

    public ImageResponseDto saveImage(MultipartFile multipartFile) throws IOException {
        String imageUrl = uploadImageComponent.upload(multipartFile, "repair-article");
        RepairArticleImage repairArticleImage = RepairArticleImage.builder()
            .imageUrl(imageUrl)
            .build();
        RepairArticleImage savedImage = repairArticleImageRepository.save(repairArticleImage);
        return ImageResponseDto.builder()
            .imageId(savedImage.getId())
            .imageUrl(savedImage.getImageUrl())
            .build();
    }

    public void deleteImage(Long imageId) {
        RepairArticleImage repairArticleImage = repairArticleImageRepository.findById(imageId)
            .orElseThrow(ImageNotFoundException::new);
        uploadImageComponent.deleteFromS3(repairArticleImage.getImageUrl());
        repairArticleImageRepository.delete(repairArticleImage);
    }
}

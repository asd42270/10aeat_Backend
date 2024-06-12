package com.final_10aeat.domain.repairArticle.controller;

import com.final_10aeat.domain.repairArticle.dto.response.ImageResponseDto;
import com.final_10aeat.domain.repairArticle.service.ManagerRepairArticleImageService;
import com.final_10aeat.global.util.ResponseDTO;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@PreAuthorize("hasRole('MANAGER')")
@RequestMapping("/managers/repair/articles")
public class ManagerRepairArticleImageController {

    private final ManagerRepairArticleImageService repairArticleImageService;

    @PostMapping("/image")
    public ResponseEntity<ResponseDTO<ImageResponseDto>> uploadImage(
        @RequestPart("image") MultipartFile multipartFile
    ) throws IOException {

        ImageResponseDto imageResponseDto = repairArticleImageService.saveImage(multipartFile);
        return ResponseEntity.ok(
            ResponseDTO.okWithData(imageResponseDto));
    }

    @DeleteMapping("/image/{imageId}")
    public ResponseEntity<ResponseDTO<Void>> deleteImage(@PathVariable Long imageId) {
        repairArticleImageService.deleteImage(imageId);
        return ResponseEntity.ok(ResponseDTO.ok());
    }
}

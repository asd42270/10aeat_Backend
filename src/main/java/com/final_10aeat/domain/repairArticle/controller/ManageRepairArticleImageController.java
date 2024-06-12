package com.final_10aeat.domain.repairArticle.controller;

import com.final_10aeat.common.dto.UserIdAndRole;
import com.final_10aeat.common.service.AuthenticationService;
import com.final_10aeat.common.service.S3ImageUploader;
import com.final_10aeat.domain.repairArticle.dto.response.ImageResponseDto;
import com.final_10aeat.domain.repairArticle.service.ManagerRepairArticleImageService;
import com.final_10aeat.global.util.ResponseDTO;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@PreAuthorize("hasRole('MANAGER')")
@RequestMapping("/managers/repair/articles")
public class ManageRepairArticleImageController {

    private final ManagerRepairArticleImageService repairArticleImageService;
    private final S3ImageUploader s3ImageUploader;
    private final AuthenticationService authenticationService;

    @PostMapping("/image")
    public ResponseEntity<ResponseDTO<ImageResponseDto>> uploadImage(
        @RequestPart("image") MultipartFile multipartFile
    ) throws IOException {

        UserIdAndRole userIdAndRole = authenticationService.getCurrentUserIdAndRole();
        String imageUrl = s3ImageUploader.upload(multipartFile, "repair-article");
        ImageResponseDto imageResponseDto = repairArticleImageService.saveImage(imageUrl,
            userIdAndRole);
        return ResponseEntity.ok(
            ResponseDTO.okWithData(imageResponseDto));
    }
}

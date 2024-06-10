package com.final_10aeat.common.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.final_10aeat.common.exception.ImageConvertException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
@RequiredArgsConstructor
public class S3ImageUploader {

    private final AmazonS3 s3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    // S3 파일 업로드
    public String upload(MultipartFile multipartFile, String dirName) throws IOException {
        // MultipartFile -> File
        File convertFile = convert(multipartFile).orElseThrow(ImageConvertException::new);

        // S3에 저장할 파일명
        String fileName = dirName + "/" + UUID.randomUUID() + "_" + convertFile.getName();

        // S3에 파일 업로드
        s3Client.putObject(new PutObjectRequest(bucket, fileName, convertFile)
            .withCannedAcl(CannedAccessControlList.PublicRead));
        String uploadImageUrl = s3Client.getUrl(bucket, fileName).toString();

        // 로컬 파일 삭제
        convertFile.delete();

        return uploadImageUrl;
    }

    // 파일 convert 후 로컬에 업로드
    private Optional<File> convert(MultipartFile file) throws IOException {
        File convertFile = new File(UUID.randomUUID()+file.getOriginalFilename());

        if (convertFile.createNewFile()) {
            try (FileOutputStream fos = new FileOutputStream(convertFile)) {
                fos.write(file.getBytes());
            }

            return Optional.of(convertFile);
        }

        return Optional.empty();
    }
}
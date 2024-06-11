package com.final_10aeat.common.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.final_10aeat.common.exception.ImageConvertException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class S3ImageUploader {

    private final AmazonS3 s3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public String upload(MultipartFile multipartFile, String dirName) throws IOException {
        File convertFile = convert(multipartFile).orElseThrow(ImageConvertException::new);

        String fileName = dirName + "/" + UUID.randomUUID() + "_" + convertFile.getName();

        s3Client.putObject(new PutObjectRequest(bucket, fileName, convertFile)
            .withCannedAcl(CannedAccessControlList.PublicRead));
        String uploadImageUrl = s3Client.getUrl(bucket, fileName).toString();

        convertFile.delete();

        return uploadImageUrl;
    }


    //로컬에 저장을 거치지 않고 InputStream을 활용하여 저장하는 방식
    public String uploadByInputStream(MultipartFile multipartFile, String dirName) {
        String fileName = dirName + "/" + UUID.randomUUID() + "_" + multipartFile.getOriginalFilename();

        try(InputStream inputStream = multipartFile.getInputStream()) {

            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(multipartFile.getSize());
            metadata.setContentType(multipartFile.getContentType());

            s3Client.putObject(new PutObjectRequest(bucket, fileName, inputStream, metadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead));

            System.out.println("s3 url = " +s3Client.getUrl(bucket, fileName).toString());
            return s3Client.getUrl(bucket, fileName).toString();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteFromS3(String imageUrl) {

        String objectKey = imageUrl.substring(imageUrl.lastIndexOf("/") + 1);

        try {
            s3Client.deleteObject(new DeleteObjectRequest(bucket, objectKey));
        } catch (Exception e) {
            throw new RuntimeException("Delete Error", e);
        }
    }



    private Optional<File> convert(MultipartFile file) throws IOException {
        File convertFile = new File(UUID.randomUUID() + file.getOriginalFilename());

        if (convertFile.createNewFile()) {
            try (FileOutputStream fos = new FileOutputStream(convertFile)) {
                fos.write(file.getBytes());
            }

            return Optional.of(convertFile);
        }

        return Optional.empty();
    }
}
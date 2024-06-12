package com.final_10aeat.domain.repairArticle.docs;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.multipart;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.partWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.request.RequestDocumentation.requestParts;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.final_10aeat.docs.RestDocsSupport;
import com.final_10aeat.domain.repairArticle.controller.ManagerRepairArticleImageController;
import com.final_10aeat.domain.repairArticle.dto.response.ImageResponseDto;
import com.final_10aeat.domain.repairArticle.service.ManagerRepairArticleImageService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

public class ManagerRepairArticleImageDocsTest extends RestDocsSupport {

    private final ManagerRepairArticleImageService repairArticleImageService = mock(
        ManagerRepairArticleImageService.class);

    @Override
    public Object initController() {
        return new ManagerRepairArticleImageController(repairArticleImageService);
    }

    @DisplayName("Repair Article Image 업로드 API 문서화")
    @Test
    void testUploadImage() throws Exception {
        MockMultipartFile imageFile = new MockMultipartFile(
            "image", "test-image.jpg", MediaType.IMAGE_JPEG_VALUE, "test image content".getBytes()
        );

        ImageResponseDto imageResponseDto = ImageResponseDto.builder()
            .imageId(1L)
            .imageUrl("http://example.com/image.jpg")
            .build();

        when(repairArticleImageService.saveImage(any(MultipartFile.class))).thenReturn(
            imageResponseDto);

        // when & then
        mockMvc.perform(multipart("/managers/repair/articles/image")
                .file(imageFile)
                .contentType(MediaType.MULTIPART_FORM_DATA))
            .andExpect(status().isOk())
            .andDo(document("upload-image",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestParts(
                    partWithName("image").description("업로드할 이미지 파일")
                ),
                responseFields(
                    fieldWithPath("code").description("응답 상태 코드"),
                    fieldWithPath("data.imageId").description("업로드된 이미지 ID"),
                    fieldWithPath("data.imageUrl").description("업로드된 이미지 URL")
                )
            ));
    }

    @DisplayName("Repair Article Image 삭제 API 문서화")
    @Test
    void testDeleteImage() throws Exception {
        // when & then
        mockMvc.perform(delete("/managers/repair/articles/image/{imageId}", 1L))
            .andExpect(status().isOk())
            .andDo(document("delete-image",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                pathParameters(
                    parameterWithName("imageId").description("이미지 ID")
                ),
                responseFields(
                    fieldWithPath("code").description("응답 상태 코드")
                )
            ));
    }
}

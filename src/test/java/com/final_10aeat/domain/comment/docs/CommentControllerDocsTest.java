package com.final_10aeat.domain.comment.docs;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.final_10aeat.common.dto.UserIdAndRole;
import com.final_10aeat.common.service.AuthUserService;
import com.final_10aeat.common.util.member.WithMember;
import com.final_10aeat.docs.RestDocsSupport;
import com.final_10aeat.domain.comment.controller.CommentController;
import com.final_10aeat.domain.comment.dto.request.CreateCommentRequestDto;
import com.final_10aeat.domain.comment.dto.request.UpdateCommentRequestDto;
import com.final_10aeat.domain.comment.dto.response.CommentResponseDto;
import com.final_10aeat.domain.comment.service.CommentService;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;

public class CommentControllerDocsTest extends RestDocsSupport {

    private CommentService commentService;
    private AuthUserService authUserService;

    @Override
    public Object initController() {
        commentService = Mockito.mock(CommentService.class);
        authUserService = Mockito.mock(AuthUserService.class);
        return new CommentController(commentService, authUserService);
    }

    @DisplayName("댓글 작성 API 문서화")
    @Test
    @WithMember
    void testCreateComment() throws Exception {
        // given
        Long repairArticleId = 1L;
        CreateCommentRequestDto requestDto = new CreateCommentRequestDto(
            null, "댓글 내용"
        );

        UserIdAndRole userIdAndRole = new UserIdAndRole(1L, false);
        when(authUserService.getCurrentUserIdAndRole()).thenReturn(userIdAndRole);

        // when
        doNothing().when(commentService).createComment(repairArticleId, requestDto, 1L, false);

        // then
        mockMvc.perform(post("/repair/articles/comments/{repairArticleId}", repairArticleId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)))
            .andExpect(status().isOk())
            .andDo(document("create-comment",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                pathParameters(
                    parameterWithName("repairArticleId").description("댓글을 작성할 게시글 ID")
                ),
                requestFields(
                    fieldWithPath("content").description("댓글 내용"),
                    fieldWithPath("parentCommentId").optional().description("부모 댓글 ID (대댓글일 경우)")
                ),
                responseFields(
                    fieldWithPath("code").description("응답 상태 코드")
                )
            ));
    }

    @DisplayName("댓글 삭제 API 문서화")
    @Test
    @WithMember
    void testDeleteComment() throws Exception {
        // given
        Long commentId = 1L;

        UserIdAndRole userIdAndRole = new UserIdAndRole(1L, false);
        when(authUserService.getCurrentUserIdAndRole()).thenReturn(userIdAndRole);

        // when
        doNothing().when(commentService).deleteComment(commentId, 1L, false);

        // then
        mockMvc.perform(delete("/repair/articles/comments/{commentId}", commentId)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(document("delete-comment",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                pathParameters(
                    parameterWithName("commentId").description("삭제할 댓글 ID")
                ),
                responseFields(
                    fieldWithPath("code").description("응답 상태 코드")
                )
            ));
    }

    @DisplayName("댓글 수정 API 문서화")
    @Test
    @WithMember
    void testUpdateComment() throws Exception {
        // given
        Long commentId = 1L;
        UpdateCommentRequestDto requestDto = new UpdateCommentRequestDto(
            "수정된 댓글 내용"
        );

        UserIdAndRole userIdAndRole = new UserIdAndRole(1L, false);
        when(authUserService.getCurrentUserIdAndRole()).thenReturn(userIdAndRole);

        // when
        doNothing().when(commentService).updateComment(commentId, requestDto, 1L, false);

        // then
        mockMvc.perform(patch("/repair/articles/comments/{commentId}", commentId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)))
            .andExpect(status().isOk())
            .andDo(document("update-comment",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                pathParameters(
                    parameterWithName("commentId").description("수정할 댓글 ID")
                ),
                requestFields(
                    fieldWithPath("content").description("수정된 댓글 내용")
                ),
                responseFields(
                    fieldWithPath("code").description("응답 상태 코드")
                )
            ));
    }

    @DisplayName("게시글별 댓글 조회 API 문서화")
    @Test
    @WithMember
    void testGetCommentsByArticleId() throws Exception {
        // given
        Long repairArticleId = 1L;

        List<CommentResponseDto> responseDtos = List.of(
            new CommentResponseDto(1L, "댓글 내용", LocalDateTime.now(), null, false, "사용자1"),
            new CommentResponseDto(2L, "대댓글 내용", LocalDateTime.now(), 1L, false, "사용자2")
        );

        // when
        when(commentService.getCommentsByArticleId(repairArticleId)).thenReturn(responseDtos);

        // then
        mockMvc.perform(get("/repair/articles/comments/{repairArticleId}", repairArticleId)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(document("get-comments-by-article-id",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                pathParameters(
                    parameterWithName("repairArticleId").description("댓글을 조회할 게시글 ID")
                ),
                responseFields(
                    fieldWithPath("code").description("응답 상태 코드"),
                    fieldWithPath("data[].id").description("댓글 ID"),
                    fieldWithPath("data[].content").description("댓글 내용"),
                    fieldWithPath("data[].createdAt").description("댓글 생성 시간"),
                    fieldWithPath("data[].parentCommentId").description("부모 댓글 ID").optional().type(
                        JsonFieldType.NUMBER),
                    fieldWithPath("data[].isAuthor").description("게시글 작성자 여부"),
                    fieldWithPath("data[].writer").description("작성자 이름")
                )
            ));
    }
}

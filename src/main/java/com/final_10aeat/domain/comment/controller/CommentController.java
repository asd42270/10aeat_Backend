package com.final_10aeat.domain.comment.controller;

import com.final_10aeat.common.dto.UserIdAndRole;
import com.final_10aeat.common.service.AuthenticationService;
import com.final_10aeat.domain.comment.dto.request.CreateCommentRequestDto;
import com.final_10aeat.domain.comment.dto.request.UpdateCommentRequestDto;
import com.final_10aeat.domain.comment.dto.response.CommentResponseDto;
import com.final_10aeat.domain.comment.service.CommentService;
import com.final_10aeat.global.util.ResponseDTO;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/repair/articles/comments")
public class CommentController {

    private final CommentService commentService;
    private final AuthenticationService authenticationService;

    @PostMapping("/{repairArticleId}")
    public ResponseEntity<ResponseDTO<Void>> createComment(
        @PathVariable Long repairArticleId,
        @RequestBody @Valid CreateCommentRequestDto request) {
        UserIdAndRole userIdAndRole = authenticationService.getCurrentUserIdAndRole();
        commentService.createComment(repairArticleId, request, userIdAndRole.id(),
            userIdAndRole.isManager());
        return ResponseEntity.ok(ResponseDTO.ok());
    }

    @PatchMapping("/{commentId}")
    public ResponseEntity<ResponseDTO<Void>> updateComment(
        @PathVariable Long commentId,
        @RequestBody @Valid UpdateCommentRequestDto request) {
        UserIdAndRole userIdAndRole = authenticationService.getCurrentUserIdAndRole();
        commentService.updateComment(commentId, request, userIdAndRole.id(),
            userIdAndRole.isManager());
        return ResponseEntity.ok(ResponseDTO.ok());
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<ResponseDTO<Void>> deleteComment(
        @PathVariable Long commentId) {
        UserIdAndRole userIdAndRole = authenticationService.getCurrentUserIdAndRole();
        commentService.deleteComment(commentId, userIdAndRole.id(), userIdAndRole.isManager());
        return ResponseEntity.ok(ResponseDTO.ok());
    }

    @GetMapping("/{repairArticleId}")
    public ResponseEntity<ResponseDTO<List<CommentResponseDto>>> getCommentsByArticleId(
        @PathVariable Long repairArticleId) {
        List<CommentResponseDto> comments = commentService.getCommentsByArticleId(repairArticleId);
        return ResponseEntity.ok(ResponseDTO.okWithData(comments));
    }
}

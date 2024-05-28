package com.final_10aeat.domain.comment.controller;

import com.final_10aeat.common.exception.UnexpectedPrincipalException;
import com.final_10aeat.domain.comment.dto.request.CreateCommentRequestDto;
import com.final_10aeat.domain.comment.dto.request.UpdateCommentRequestDto;
import com.final_10aeat.domain.comment.dto.request.UserIdAndRole;
import com.final_10aeat.domain.comment.service.CommentService;
import com.final_10aeat.global.security.principal.ManagerPrincipal;
import com.final_10aeat.global.security.principal.MemberPrincipal;
import com.final_10aeat.global.util.ResponseDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
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

    @PostMapping("/{repairArticleId}")
    public ResponseEntity<ResponseDTO<Void>> createComment(
        @PathVariable Long repairArticleId,
        @RequestBody @Valid CreateCommentRequestDto request) {
        UserIdAndRole userIdAndRole = getCurrentIdAndRole();
        commentService.createComment(repairArticleId, request, userIdAndRole.getId(),
            userIdAndRole.isManager());
        return ResponseEntity.ok(ResponseDTO.ok());
    }

    @PatchMapping("/{commentId}")
    public ResponseEntity<ResponseDTO<Void>> updateComment(
        @PathVariable Long commentId,
        @RequestBody @Valid UpdateCommentRequestDto request) {
        UserIdAndRole userIdAndRole = getCurrentIdAndRole();
        commentService.updateComment(commentId, request, userIdAndRole.getId(),
            userIdAndRole.isManager());
        return ResponseEntity.ok(ResponseDTO.ok());
    }

    private UserIdAndRole getCurrentIdAndRole() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof MemberPrincipal) {
            return new UserIdAndRole(((MemberPrincipal) principal).getMember().getId(), false);
        } else if (principal instanceof ManagerPrincipal) {
            return new UserIdAndRole(((ManagerPrincipal) principal).getManager().getId(), true);
        }
        throw new UnexpectedPrincipalException();
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<ResponseDTO<Void>> deleteComment(
        @PathVariable Long commentId) {
        UserIdAndRole userIdAndRole = getCurrentIdAndRole();
        commentService.deleteComment(commentId, userIdAndRole.getId(), userIdAndRole.isManager());
        return ResponseEntity.ok(ResponseDTO.ok());
    }
}

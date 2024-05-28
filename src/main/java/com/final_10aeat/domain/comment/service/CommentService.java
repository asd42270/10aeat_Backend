package com.final_10aeat.domain.comment.service;

import com.final_10aeat.common.exception.ArticleNotFoundException;
import com.final_10aeat.common.exception.UnauthorizedAccessException;
import com.final_10aeat.domain.comment.dto.request.CreateCommentRequestDto;
import com.final_10aeat.domain.comment.dto.request.UpdateCommentRequestDto;
import com.final_10aeat.domain.comment.entity.Comment;
import com.final_10aeat.domain.comment.exception.CommentNotFoundException;
import com.final_10aeat.domain.comment.exception.InvalidCommentDepthException;
import com.final_10aeat.domain.comment.exception.ParentCommentNotFoundException;
import com.final_10aeat.domain.comment.repository.CommentRepository;
import com.final_10aeat.domain.manager.entity.Manager;
import com.final_10aeat.domain.manager.repository.ManagerRepository;
import com.final_10aeat.domain.member.entity.Member;
import com.final_10aeat.domain.member.exception.UserNotExistException;
import com.final_10aeat.domain.member.repository.MemberRepository;
import com.final_10aeat.domain.repairArticle.entity.RepairArticle;
import com.final_10aeat.domain.repairArticle.exception.ManagerNotFoundException;
import com.final_10aeat.domain.repairArticle.repository.RepairArticleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final RepairArticleRepository repairArticleRepository;
    private final MemberRepository memberRepository;
    private final ManagerRepository managerRepository;

    public void createComment(Long repairArticleId, CreateCommentRequestDto request, Long userId,
        boolean isManager) {
        RepairArticle repairArticle = repairArticleRepository.findById(repairArticleId)
            .orElseThrow(ArticleNotFoundException::new);

        Comment.CommentBuilder commentBuilder = Comment.builder()
            .content(request.content())
            .repairArticle(repairArticle)
            .parentComment(null);

        if (isManager) {
            Manager manager = managerRepository.findById(userId)
                .orElseThrow(ManagerNotFoundException::new);
            commentBuilder.manager(manager);
        } else {
            Member member = memberRepository.findById(userId)
                .orElseThrow(UserNotExistException::new);
            commentBuilder.member(member);
        }

        Comment parentComment = getParentComment(request.parentCommentId());

        if (parentComment != null && parentComment.getParentComment() != null) {
            throw new InvalidCommentDepthException();
        }

        commentBuilder.parentComment(parentComment != null ? parentComment.getId() : null);

        commentRepository.save(commentBuilder.build());
    }

    private Comment getParentComment(Long parentCommentId) {
        if (parentCommentId == null) {
            return null;
        }
        return commentRepository.findById(parentCommentId)
            .orElseThrow(ParentCommentNotFoundException::new);
    }

    public void updateComment(Long commentId, UpdateCommentRequestDto request, Long userId,
        boolean isManager) {
        Comment comment = commentRepository.findById(commentId)
            .orElseThrow(CommentNotFoundException::new);

        checkAuthorization(comment, userId, isManager);

        comment.setContent(request.content());
        commentRepository.save(comment);
    }

    private void checkAuthorization(Comment comment, Long userId, boolean isManager) {
        if (isManager) {
            if (comment.getManager() == null || !comment.getManager().getId().equals(userId)) {
                throw new UnauthorizedAccessException();
            }
        } else {
            if (comment.getMember() == null || !comment.getMember().getId().equals(userId)) {
                throw new UnauthorizedAccessException();
            }
        }
    }
}

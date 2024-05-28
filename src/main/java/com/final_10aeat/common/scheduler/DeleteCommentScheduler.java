package com.final_10aeat.common.scheduler;

import com.final_10aeat.domain.comment.entity.Comment;
import com.final_10aeat.domain.comment.repository.CommentRepository;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DeleteCommentScheduler {

    private final CommentRepository commentRepository;

    @Scheduled(cron = "0 0 2 * * ?")
    public void deleteOldSoftDeletedComments() {
        LocalDateTime oneYearAgo = LocalDateTime.now().minusYears(1);
        List<Comment> commentsToDelete = commentRepository.findSoftDeletedBefore(oneYearAgo);

        commentRepository.deleteAll(commentsToDelete);
    }
}

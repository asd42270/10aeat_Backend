package com.final_10aeat.domain.comment.repository;

import com.final_10aeat.domain.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {

}

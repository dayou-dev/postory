package com.dayou.postory.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dayou.postory.domain.comment.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {
	Comment findByIdAndPostId(Long commentId, Long postId);
}

package com.dayou.postory.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dayou.postory.domain.comment.Comment;
import com.dayou.postory.domain.post.Post;

public interface CommentRepository extends JpaRepository<Comment, Long> {

	Comment findByIdAndPost(Long commentId, Post post);
}

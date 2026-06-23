package com.dayou.postory.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dayou.postory.domain.post.Post;
import com.dayou.postory.domain.user.User;

public interface PostRepository extends JpaRepository<Post, Long> {
	Post findByUserAndId(User user, Long postId);
}

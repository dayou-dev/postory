package com.dayou.postory.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dayou.postory.domain.like.Like;
import com.dayou.postory.domain.post.Post;
import com.dayou.postory.domain.user.User;

public interface LikeRepository extends JpaRepository<Like, Long> {
	Like findByUserAndPost(User user, Post post);

	boolean existsByUserIdAndPostId(Long userId, Long postId);
}

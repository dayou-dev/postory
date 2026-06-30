package com.dayou.postory.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.dayou.postory.domain.post.Post;
import com.dayou.postory.domain.user.User;

public interface PostRepository extends JpaRepository<Post, Long> {
	Post findByUserAndId(User user, Long postId);
	@Query("select p from Post p join fetch p.user")
	Page<Post> findAll(Pageable pageable);
}

package com.dayou.postory.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dayou.postory.domain.post.Post;

public interface PostRepository extends JpaRepository<Post, Long> {
}

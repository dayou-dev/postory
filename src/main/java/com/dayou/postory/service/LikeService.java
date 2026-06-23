package com.dayou.postory.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dayou.postory.domain.like.Like;
import com.dayou.postory.domain.post.Post;
import com.dayou.postory.domain.user.User;
import com.dayou.postory.global.response.exception.ErrorCode;
import com.dayou.postory.global.response.exception.UserNotFoundException;
import com.dayou.postory.repository.LikeRepository;
import com.dayou.postory.repository.PostRepository;
import com.dayou.postory.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class LikeService {
	private final LikeRepository likeRepository;
	private final PostRepository postRepository;
	private final UserRepository userRepository;

	public void likePost(Long userId, Long postId) {
		User user = userRepository.findById(userId)
			.orElseThrow(() -> new UserNotFoundException(ErrorCode.USER_NOT_FOUND));
		Post post = postRepository.findByUserAndId(user, postId);
		Like like = Like.builder().post(post).user(user).build();
		likeRepository.save(like);
	}

	public void unlikePost(Long userId, Long postId) {
		User user = userRepository.findById(userId)
			.orElseThrow(() -> new UserNotFoundException(ErrorCode.USER_NOT_FOUND));
		Post post = postRepository.findByUserAndId(user, postId);
		Like like = likeRepository.findByUserAndPost(user, post);
		likeRepository.delete(like);
	}
}
